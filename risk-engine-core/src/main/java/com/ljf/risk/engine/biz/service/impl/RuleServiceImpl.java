package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.ljf.risk.engine.biz.core.component.InitComponent;
import com.ljf.risk.engine.biz.core.component.RuleComponent;
import com.ljf.risk.engine.biz.core.constants.CurrentContext;
import com.ljf.risk.engine.biz.dao.RuleDao;
import com.ljf.risk.engine.biz.service.ConditionGroupService;
import com.ljf.risk.engine.biz.service.ConditionService;
import com.ljf.risk.engine.biz.service.EventRuleRelationService;
import com.ljf.risk.engine.biz.service.EventService;
import com.ljf.risk.engine.biz.service.PunishService;
import com.ljf.risk.engine.biz.service.RuleService;
import com.ljf.risk.engine.common.entity.Condition;
import com.ljf.risk.engine.common.entity.ConditionGroup;
import com.ljf.risk.engine.common.entity.EngineCheckReq;
import com.ljf.risk.engine.common.entity.EngineCheckRes;
import com.ljf.risk.engine.common.entity.Event;
import com.ljf.risk.engine.common.entity.EventRuleRelation;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.Punish;
import com.ljf.risk.engine.common.entity.Rule;
import com.ljf.risk.engine.common.entity.constants.ConditionRelationType;
import com.ljf.risk.engine.common.entity.request.TestRuleReq;
import com.ljf.risk.engine.common.entity.response.RelationDataRes;
import com.ljf.risk.engine.common.entity.response.TestResponse;
import com.ljf.risk.engine.common.utils.SpringBootBeanUtil;
import com.ljf.risk.engine.common.utils.ThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author lijinfeng
 */
@Service
public class RuleServiceImpl extends ServiceImpl<RuleDao, Rule> implements RuleService {
    private final EventService eventService;

    private final EventRuleRelationService eventRuleRelationService;

    private final PunishService punishService;

    private final ConditionService conditionService;

    private final ConditionGroupService conditionGroupService;

    public RuleServiceImpl(EventService eventService, EventRuleRelationService eventRuleRelationService, PunishService punishService, ConditionService conditionService, ConditionGroupService conditionGroupService) {
        this.eventService = eventService;
        this.eventRuleRelationService = eventRuleRelationService;
        this.punishService = punishService;
        this.conditionService = conditionService;
        this.conditionGroupService = conditionGroupService;
    }

    @Override
    public IPage<Rule> pageRule(PageReq<Rule> rulePageReq) {
        LambdaQueryWrapper<Rule> qw = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(rulePageReq.getData().getCode())) {
            qw.like(Rule::getCode, rulePageReq.getData().getCode());
        }
        if (!Objects.isNull(rulePageReq.getData().getStatus())) {
            qw.eq(Rule::getStatus, rulePageReq.getData().getStatus());
        }
        if (!Objects.isNull(rulePageReq.getData().getResult())) {
            qw.eq(Rule::getResult, rulePageReq.getData().getCode());
        }
        if (!Objects.isNull(rulePageReq.getData().getRuleGroupId())) {
            qw.eq(Rule::getRuleGroupId, rulePageReq.getData().getRuleGroupId());
        }
        qw.orderByDesc(Rule::getCreateTime);
        return page(new Page<>(rulePageReq.getCurrent(), rulePageReq.getSize()), qw);
    }

    @Override
    public boolean update(Rule rule) {
        rule.setUpdateBy(ThreadLocalUtils.getUsername());
        rule.setStatus(Rule.Status.OFFLINE);
        return updateById(rule);
    }

    @Override
    public boolean delete(Rule rule) {
        return removeById(rule);
    }

    @Override
    public boolean add(Rule rule) {
        boolean exists = baseMapper.exists(new LambdaQueryWrapper<Rule>().eq(Rule::getCode, rule.getCode()));
        if (exists) {
            throw new RuntimeException("该规则已存在");
        }
        String username = ThreadLocalUtils.getUsername();
        rule.setStatus(Rule.Status.OFFLINE);
        rule.setCreateBy(username);
        rule.setUpdateBy(username);
        return save(rule);
    }

    @Override
    public RelationDataRes relationData(Long ruleId) {
        List<Event> events = eventService.list();
        List<EventRuleRelation> eventRuleRelations = eventRuleRelationService.list(new LambdaQueryWrapper<EventRuleRelation>().eq(EventRuleRelation::getRuleId, ruleId));

        Map<String, String> punishTypes = new HashMap<>();
        for (Punish.PunishType value : Punish.PunishType.values()) {
            punishTypes.put(value.name(), value.getDescription());
        }
        List<Punish> punishes = punishService.list(new LambdaQueryWrapper<Punish>().eq(Punish::getRuleId, ruleId));
        return RelationDataRes.builder()
                .punishTypes(punishTypes)
                .eventRuleRelations(eventRuleRelations)
                .events(events)
                .punishs(punishes)
                .build();
    }

    @Override
    public Set<String> ruleAllAttribute(Long ruleId) {
        List<Condition> conditions = conditionService.listCondition(ruleId, ConditionRelationType.RULE);
        List<ConditionGroup> conditionGroups = conditionGroupService.listConditionGroup(ruleId, ConditionRelationType.RULE);
        for (ConditionGroup conditionGroup : conditionGroups) {
            conditions.addAll(conditionGroup.getConditionList());
        }
        Set<String> temp = new HashSet<>();
        for (Condition condition : conditions) {
            if (Condition.PropertyType.CONTEXT_PROPERTY.equals(condition.getLeftType())) {
                temp.add(condition.getLeftProperty());
            }
            if (Condition.PropertyType.CONTEXT_PROPERTY.equals(condition.getRightType())) {
                temp.add(condition.getRightProperty());
            }
        }
        return temp;
    }

    @Override
    public TestResponse testRule(@RequestBody TestRuleReq testRuleReq) {
        try {
            if (Objects.isNull(testRuleReq.getRuleId())) {
                throw new RuntimeException("参数必填");
            }
            Rule rule = getOne(new LambdaQueryWrapper<Rule>().eq(Rule::getId, testRuleReq.getRuleId()));
            if (rule == null) {
                throw new RuntimeException("无相关规则");
            }
            InitComponent bean = SpringBootBeanUtil.getBean(InitComponent.class);
            boolean load = bean.load();
            if (!load) {
                throw new RuntimeException("加载规则失败");
            }
            EngineCheckReq build = EngineCheckReq.builder().eventCode(null).eventDetails(testRuleReq.getAttributes()).bizId(UUID.randomUUID().toString()).build();
            try (CurrentContext ignored = new CurrentContext(CurrentContext.Context.builder().eventTime(new Date()).engineCheckReq(build).build())) {
                EngineCheckRes analyse = SpringBootBeanUtil.getBean(RuleComponent.class).analyse(Lists.newArrayList(rule));
                updateById(Rule.builder().id(testRuleReq.getRuleId()).test(true).build());
                return TestResponse.builder()
                        .ruleResult(analyse.getCheckResult().getMsg())
                        .functionResult(CurrentContext.currentCtx().getFunctionResult())
                        .indicatorResult(CurrentContext.currentCtx().getIndicatorResult())
                        .conditionResult(CurrentContext.currentCtx().getConditionResult())
                        .checkResult(analyse.getCheckResult())
                        .build();
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException("测试失败");
        }
    }

    @Override
    public boolean updateStatus(Rule rule) {
        Rule temp = getById(rule);
        rule.setUpdateBy(ThreadLocalUtils.getUsername());
        if (Rule.Status.ONLINE.equals(temp.getStatus())) {
            rule.setStatus(Rule.Status.OFFLINE);
            return updateById(rule);
        }
        if (temp.getTest()) {
            rule.setStatus(Rule.Status.ONLINE);
            return updateById(rule);
        }
        throw new RuntimeException("该规则未测试");
    }
}



package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.dao.EventDao;
import com.ljf.risk.engine.biz.service.EventIndicatorRelationService;
import com.ljf.risk.engine.biz.service.EventRuleRelationService;
import com.ljf.risk.engine.biz.service.EventService;
import com.ljf.risk.engine.biz.service.RuleService;
import com.ljf.risk.engine.common.entity.Event;
import com.ljf.risk.engine.common.entity.EventIndicatorRelation;
import com.ljf.risk.engine.common.entity.EventRuleRelation;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.Rule;
import com.ljf.risk.engine.common.entity.request.RelationRuleReq;
import com.ljf.risk.engine.common.entity.response.RelationRuleRes;
import com.ljf.risk.engine.common.utils.ThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventDao, Event> implements EventService {
    @Autowired
    private RuleService ruleService;

    @Autowired
    private EventRuleRelationService eventRuleRelationService;

    @Autowired
    private EventIndicatorRelationService eventIndicatorRelationService;

    @Override
    public IPage<Event> pageEvent(PageReq<Event> event) {
        QueryWrapper<Event> qw = new QueryWrapper<>();
        if (StringUtils.isNotBlank(event.getData().getCode())) {
            qw.like("code", event.getData().getCode());
        }
        if (!Objects.isNull(event.getData().getAccumulate())) {
            qw.eq("accumulate", event.getData().getAccumulate());
        }
        if (!Objects.isNull(event.getData().getAnalysis())) {
            qw.eq("analysis", event.getData().getAnalysis());
        }
        if (!Objects.isNull(event.getData().getStatus())) {
            qw.eq("status", event.getData().getStatus());
        }
        qw.orderByDesc("update_time");
        return page(new Page<>(event.getCurrent(), event.getSize()), qw);
    }

    @Override
    public boolean update(Event event) {
        event.setUpdateBy(ThreadLocalUtils.getUsername());
        return updateById(event);
    }

    @Override
    public boolean delete(Event event) {
        long count = eventRuleRelationService.count(new LambdaQueryWrapper<EventRuleRelation>().eq(EventRuleRelation::getEventId, event.getId()));
        if (count > 0) {
            throw new RuntimeException("删除失败:该事件下已绑定规则");
        }
        long count2 = eventIndicatorRelationService.count(new LambdaQueryWrapper<EventIndicatorRelation>().eq(EventIndicatorRelation::getEventId, event.getId()));
        if (count2 > 0) {
            throw new RuntimeException("删除失败:该事件下已绑定指标");
        }
        return removeById(event);
    }

    @Override
    public boolean add(Event event) {
        boolean exists = baseMapper.exists(new QueryWrapper<Event>().eq("code", event.getCode()));
        if (exists) {
            throw new RuntimeException("该事件已存在");
        }
        String username = ThreadLocalUtils.getUsername();
        event.setCreateBy(username);
        event.setUpdateBy(username);
        return save(event);
    }

    @Override
    public RelationRuleRes pageRelationRule(PageReq<RelationRuleReq> relationRuleReqPageReq) {
        PageReq<Rule> rulePageReq = PageReq.<Rule>builder()
                .current(relationRuleReqPageReq.getCurrent())
                .size(relationRuleReqPageReq.getSize())
                .data(Rule.builder().code(relationRuleReqPageReq.getData().getRuleCode()).build())
                .build();
        IPage<Rule> ruleIPage = ruleService.pageRule(rulePageReq);
        List<Long> ruleIds = eventRuleRelationService.list(new LambdaQueryWrapper<EventRuleRelation>()
                        .eq(EventRuleRelation::getEventId, relationRuleReqPageReq.getData().getEventId()))
                .stream()
                .map(EventRuleRelation::getRuleId)
                .collect(Collectors.toList());

        return RelationRuleRes.builder()
                .rulePage(ruleIPage)
                .ruleIds(ruleIds)
                .build();
    }

}

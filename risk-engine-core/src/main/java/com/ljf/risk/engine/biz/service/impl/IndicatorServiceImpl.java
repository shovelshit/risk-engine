package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.dao.IndicatorDao;
import com.ljf.risk.engine.biz.service.ConditionGroupService;
import com.ljf.risk.engine.biz.service.ConditionService;
import com.ljf.risk.engine.biz.service.EventIndicatorRelationService;
import com.ljf.risk.engine.biz.service.EventService;
import com.ljf.risk.engine.biz.service.IndicatorService;
import com.ljf.risk.engine.common.entity.Condition;
import com.ljf.risk.engine.common.entity.ConditionGroup;
import com.ljf.risk.engine.common.entity.Event;
import com.ljf.risk.engine.common.entity.EventIndicatorRelation;
import com.ljf.risk.engine.common.entity.Indicator;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.constants.ConditionRelationType;
import com.ljf.risk.engine.common.entity.response.RelationDataRes;
import com.ljf.risk.engine.common.utils.ThreadLocalUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Service
public class IndicatorServiceImpl extends ServiceImpl<IndicatorDao, Indicator> implements IndicatorService {
    private final EventService eventService;

    private final EventIndicatorRelationService eventIndicatorRelationService;

    private final ConditionGroupService conditionGroupService;

    private final ConditionService conditionService;

//    private final IndicatorAdmin indicatorAdmin;

    public IndicatorServiceImpl(EventService eventService, EventIndicatorRelationService eventIndicatorRelationService, ConditionGroupService conditionGroupService, ConditionService conditionService) {
        this.eventService = eventService;
        this.eventIndicatorRelationService = eventIndicatorRelationService;
        this.conditionGroupService = conditionGroupService;
        this.conditionService = conditionService;
    }

    @Override
    public IPage<Indicator> pageData(PageReq<Indicator> indicatorPageReq) {
        LambdaQueryWrapper<Indicator> qw = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(indicatorPageReq.getData().getCode())) {
            qw.eq(Indicator::getCode, indicatorPageReq.getData().getCode());
        }
        qw.orderByDesc(Indicator::getUpdateTime);
        return page(new Page<>(indicatorPageReq.getCurrent(), indicatorPageReq.getSize()), qw);
    }

    @Override
    public boolean update(Indicator indicator) {
        indicator.setUpdateBy(ThreadLocalUtils.getUsername());
        return updateById(indicator);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Indicator indicator) {
        if (eventIndicatorRelationService.count(new LambdaQueryWrapper<EventIndicatorRelation>().eq(EventIndicatorRelation::getIndicatorId, indicator.getId())) > 0) {
            throw new RuntimeException("删除失败: 已关联事件");
        }
        conditionService.remove(new LambdaQueryWrapper<Condition>().eq(Condition::getRelationId, indicator.getId()).eq(Condition::getRelationType, ConditionRelationType.INDICATOR));
        LambdaQueryWrapper<ConditionGroup> conditionGroupLambdaQueryWrapper = new LambdaQueryWrapper<ConditionGroup>().eq(ConditionGroup::getRelationId, indicator.getId()).eq(ConditionGroup::getRelationType, ConditionRelationType.INDICATOR);
        List<ConditionGroup> list = conditionGroupService.list(conditionGroupLambdaQueryWrapper);
        List<Long> conditionGroupIds = list.stream().map(ConditionGroup::getId).collect(Collectors.toList());
        conditionGroupService.remove(conditionGroupLambdaQueryWrapper);
        if (CollectionUtils.isNotEmpty(conditionGroupIds)) {
            conditionService.remove(new LambdaQueryWrapper<Condition>().in(Condition::getRelationId, conditionGroupIds).eq(Condition::getRelationType, ConditionRelationType.CONDITION_GROUP));
        }
        return removeById(indicator);
    }

    @Override
    public boolean add(Indicator indicator) {
        boolean exists = baseMapper.exists(new LambdaQueryWrapper<Indicator>().eq(Indicator::getCode, indicator.getCode()));
        if (exists) {
            throw new RuntimeException("该指标已存在");
        }
        String username = ThreadLocalUtils.getUsername();
        indicator.setCreateBy(username);
        indicator.setUpdateBy(username);
        return save(indicator);
    }

    @Override
    public RelationDataRes relationData(Long indicatorId) {
        List<Event> events = eventService.list();
        List<EventIndicatorRelation> eventRuleRelations = eventIndicatorRelationService.list(new LambdaQueryWrapper<EventIndicatorRelation>().eq(EventIndicatorRelation::getIndicatorId, indicatorId));
        return RelationDataRes.builder()
                .eventIndicatorRelations(eventRuleRelations)
                .events(events)
                .build();
    }

//    @Override
//    public Object testIndicator(IndicatorAdmin.TestReq testReq) {
//        PlayLoadResponse<IndicatorAdmin.TestResponse> testResponsePlayLoadResponse = indicatorAdmin.testIndicator(testReq);
//        if (testResponsePlayLoadResponse.isSuccess()) {
//            return testResponsePlayLoadResponse.getPlayLoad();
//        }
//        throw new RuntimeException(testResponsePlayLoadResponse.getMsg());
//    }
}


package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.dao.EventRuleRelationDao;
import com.ljf.risk.engine.biz.service.EventRuleRelationService;
import com.ljf.risk.engine.common.entity.EventRuleRelation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventRuleRelationServiceImpl extends ServiceImpl<EventRuleRelationDao, EventRuleRelation> implements EventRuleRelationService {
    @Override
    public boolean delete(EventRuleRelation eventRuleGroupRelation) {
        return remove(new LambdaQueryWrapper<EventRuleRelation>().eq(EventRuleRelation::getRuleId, eventRuleGroupRelation.getRuleId()).eq(EventRuleRelation::getEventId, eventRuleGroupRelation.getEventId()));
    }

    @Override
    public boolean add(EventRuleRelation eventRuleGroupRelation) {
        return save(eventRuleGroupRelation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean relationEvents(List<Long> eventIds, Long ruleId) {
        ArrayList<EventRuleRelation> data = new ArrayList<>();
        ArrayList<EventRuleRelation> temp = new ArrayList<>();
        for (Long eventId : eventIds) {
            data.add(EventRuleRelation.builder().eventId(eventId).ruleId(ruleId).build());
            temp.add(EventRuleRelation.builder().eventId(eventId).ruleId(ruleId).build());
        }
        List<EventRuleRelation> exists = list(new LambdaQueryWrapper<EventRuleRelation>().eq(EventRuleRelation::getRuleId, ruleId));

        // to add
        data.removeAll(exists);
        if (CollectionUtils.isNotEmpty(data)) {
            saveBatch(data);
        }

        // remove
        exists.removeAll(temp);
        if (CollectionUtils.isNotEmpty(exists)) {
            removeBatchByIds(exists);
        }

        return true;
    }
}



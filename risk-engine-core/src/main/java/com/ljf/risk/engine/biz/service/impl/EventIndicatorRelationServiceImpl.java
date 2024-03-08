package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.dao.EventIndicatorRelationDao;
import com.ljf.risk.engine.biz.service.EventIndicatorRelationService;
import com.ljf.risk.engine.common.entity.EventIndicatorRelation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lijinfeng
 */
@Service
public class EventIndicatorRelationServiceImpl extends ServiceImpl<EventIndicatorRelationDao, EventIndicatorRelation> implements EventIndicatorRelationService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean relationEvents(List<Long> eventIds, Long indicatorId) {
        ArrayList<EventIndicatorRelation> data = new ArrayList<>();
        ArrayList<EventIndicatorRelation> temp = new ArrayList<>();
        for (Long eventId : eventIds) {
            data.add(EventIndicatorRelation.builder().eventId(eventId).indicatorId(indicatorId).build());
            temp.add(EventIndicatorRelation.builder().eventId(eventId).indicatorId(indicatorId).build());
        }
        List<EventIndicatorRelation> exists = list(new LambdaQueryWrapper<EventIndicatorRelation>().eq(EventIndicatorRelation::getIndicatorId, indicatorId));

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



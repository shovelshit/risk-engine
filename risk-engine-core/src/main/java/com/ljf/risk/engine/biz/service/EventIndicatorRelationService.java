package com.ljf.risk.engine.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.EventIndicatorRelation;

import java.util.List;

/**
 * @author lijinfeng
 */
public interface EventIndicatorRelationService extends IService<EventIndicatorRelation> {
    boolean relationEvents(List<Long> eventIds, Long indicatorId);
}

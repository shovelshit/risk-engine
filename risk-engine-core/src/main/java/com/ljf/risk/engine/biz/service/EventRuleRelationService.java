package com.ljf.risk.engine.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.EventRuleRelation;

import java.util.List;

/**
 * @author lijinfeng
 */
public interface EventRuleRelationService extends IService<EventRuleRelation> {
    boolean delete(EventRuleRelation eventRuleRelation);

    boolean add(EventRuleRelation eventRuleRelation);

    boolean relationEvents(List<Long> eventIds, Long ruleId);

}

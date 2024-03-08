package com.ljf.risk.engine.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.ConditionGroup;
import com.ljf.risk.engine.common.entity.constants.ConditionRelationType;

import java.util.List;

/**
 * @author lijinfeng
 */
public interface ConditionGroupService extends IService<ConditionGroup> {
    List<ConditionGroup> listConditionGroup(Long relationId, ConditionRelationType conditionRelationType);

}

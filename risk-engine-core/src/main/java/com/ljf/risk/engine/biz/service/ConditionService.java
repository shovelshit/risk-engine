package com.ljf.risk.engine.biz.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.Condition;
import com.ljf.risk.engine.common.entity.constants.ConditionRelationType;
import com.ljf.risk.engine.common.entity.request.createConditionsReq;

import java.util.List;

/**
 * @author lijinfeng
 */
public interface ConditionService extends IService<Condition> {
    List<Condition> listCondition(Long relationId, ConditionRelationType conditionRelationType);

    Boolean createConditions(createConditionsReq req);
}

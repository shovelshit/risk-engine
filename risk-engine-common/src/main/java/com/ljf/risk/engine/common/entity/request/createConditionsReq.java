package com.ljf.risk.engine.common.entity.request;

import com.ljf.risk.engine.common.entity.ConditionGroup;
import com.ljf.risk.engine.common.entity.Condition;
import com.ljf.risk.engine.common.entity.constants.ConditionRelationType;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;
import javax.validation.constraints.NotNull;

@Data
@Valid
public class createConditionsReq {
    @NotNull
    private Long relationId;

    @NotNull
    private ConditionRelationType relationType;

    private List<Condition> conditions;

    private List<ConditionGroup> conditionGroups;
}
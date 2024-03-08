package com.ljf.risk.engine.common.entity.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Valid
public class RelationRuleReq {
    @NotNull
    private Long eventId;

    private String ruleCode;
}
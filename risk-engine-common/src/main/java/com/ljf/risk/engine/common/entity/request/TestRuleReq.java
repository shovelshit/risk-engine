package com.ljf.risk.engine.common.entity.request;

import lombok.Data;
import lombok.ToString;
import java.util.Map;

@ToString
@Data
public class TestRuleReq {
    private Long ruleId;
    private Map<String, Object> attributes;
}
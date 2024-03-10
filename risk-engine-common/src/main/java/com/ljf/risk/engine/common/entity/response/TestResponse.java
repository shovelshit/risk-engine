package com.ljf.risk.engine.common.entity.response;

import com.ljf.risk.engine.common.entity.CheckResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestResponse {
    private String ruleResult;
    private CheckResult checkResult;
    private Map<String, Object> functionResult;
    private Map<String, Object> conditionResult;
    private Map<String, Object> indicatorResult;
}
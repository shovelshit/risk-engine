package com.ljf.risk.engine.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckDetails {
    private Integer failedCode;
    private String failedMsg;
    private List<String> hitRules;
    private String hitRule;
    private Map<String, Object> conditionResult;
}
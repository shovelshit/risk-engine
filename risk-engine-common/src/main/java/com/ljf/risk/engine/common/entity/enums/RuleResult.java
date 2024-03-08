package com.ljf.risk.engine.common.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public enum RuleResult {
    REJECT(1001, "拒绝"),
    PASS(0, "通过");

    @EnumValue
    private int code;

    @JsonValue
    private String description;


    public static boolean pass(RuleResult result) {
        return PASS.equals(result);
    }

    public static boolean reject(RuleResult result) {
        return REJECT.equals(result);
    }
}
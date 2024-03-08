package com.ljf.risk.engine.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckResult {
    private String eventCode;
    private String eventCodeDesc;
    private Integer code;
    private String msg;
    private CheckDetails details;

    public boolean isReject() {
        return code.equals(1001);
    }
}
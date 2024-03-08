package com.ljf.risk.engine.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineCheckReq {
    private String eventCode;
    private String bizId;
    private Map<String, Object> eventDetails;
}
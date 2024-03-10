package com.ljf.risk.engine.common.entity.request;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@ToString
@Data
public class TestReq {
    private Long indicatorId;
    private String period;
    private Map<String, Object> attributes;
}
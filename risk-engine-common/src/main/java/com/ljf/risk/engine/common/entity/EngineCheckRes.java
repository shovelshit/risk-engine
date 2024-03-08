package com.ljf.risk.engine.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineCheckRes {
    private Integer code;
    private String msg;
    private CheckResult checkResult;
    private String original;

    public static EngineCheckRes failed(Result result) {
        return EngineCheckRes.builder().code(result.getCode()).msg(result.getMsg()).build();
    }

    public static EngineCheckRes success() {
        return EngineCheckRes.builder().code(Result.SUCCESS.getCode()).msg(Result.SUCCESS.getMsg()).build();
    }

    public static EngineCheckRes success(CheckResult checkResult) {
        return EngineCheckRes.builder().code(Result.SUCCESS.getCode()).msg(Result.SUCCESS.getMsg()).build().checkResult(checkResult);
    }

    public EngineCheckRes checkResult(CheckResult checkResult) {
        setCheckResult(checkResult);
        return this;
    }


    public boolean isSuccess() {
        return code.equals(Result.SUCCESS.getCode());
    }

    @ToString
    @Getter
    @AllArgsConstructor
    enum Result {
        SUCCESS(200, "成功"),
        ERROR(500, "内部错误");

        private Integer code;

        private String msg;

    }






}
package com.ljf.risk.engine.common.utils;

import java.util.Map;

/**
 * @author lijinfeng
 */
public class ThreadLocalUtils implements AutoCloseable {

    final static ThreadLocal<Map<String, Object>> ctx = new ThreadLocal<>();

    public ThreadLocalUtils(Map<String, Object> value) {
        ctx.set(value);
    }

    public static Map<String, Object> currentCtx() {
        return ctx.get();
    }

    public static String getUsername() {
        Map<String, Object> map = ctx.get();
        return "admin";
    }

    @Override
    public void close() {
        ctx.remove();
    }
}

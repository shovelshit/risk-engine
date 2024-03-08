package com.ljf.risk.engine.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ljf.risk.engine.common",
        "com.ljf.risk.engine.web", "com.ljf.risk.engine.biz"})
@MapperScan(value = {"com.ljf.risk.engine.web.dao", "com.ljf.risk.engine.biz.dao"})
public class EngineBoostrap {
    public static void main(String[] args) {
        SpringApplication.run(EngineBoostrap.class);
    }
}

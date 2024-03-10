package com.ljf.risk.engine.web;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Order(0)
public class H2Init implements InitializingBean {

    private final DataSource dataSource;

    public H2Init(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建一个资源初始化器
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        // 添加SQL脚本资源
        populator.addScript(new ClassPathResource("db/schema-h2.sql"));
        populator.addScript(new ClassPathResource("db/data-h2.sql"));
        // 执行脚本
        DatabasePopulatorUtils.execute(populator, dataSource);
    }
}

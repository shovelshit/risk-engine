package com.ljf.risk.engine.biz.core.component;

import com.ljf.risk.engine.biz.core.SpringContextComponent;
import com.ljf.risk.engine.biz.core.component.load.AbstractComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class InitComponent implements InitializingBean {

    private final SpringContextComponent springContextComponent;
    private final DataSource dataSource;

    public InitComponent(SpringContextComponent springContextComponent, DataSource dataSource) {
        this.springContextComponent = springContextComponent;
        this.dataSource = dataSource;
    }

    public boolean load() {
        long startTime = System.currentTimeMillis();
        try {
            ObjectProvider<AbstractComponent> beanProvider = springContextComponent.getBeanProvider(AbstractComponent.class);
            List<AbstractComponent> beans = beanProvider.orderedStream().collect(Collectors.toList());
            for (AbstractComponent loadComponent : beans) {
                loadComponent.beforeLoad();
                loadComponent.load();
                loadComponent.afterLoad();
            }
            return true;
        } catch (Exception e) {
            log.error("ComponentLoad, 规则引擎初始数据加载出错, cost: {}ms", (System.currentTimeMillis() - startTime), e);
        } finally {
            log.info("ComponentLoad, 规则引擎初始数据加载完成, cost: {}ms", System.currentTimeMillis() - startTime);
        }
        return false;
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
        if(!load()){
            throw new Exception("ComponentLoad, 规则引擎核心数据初始化有误,停止项目启动,开发人员检查后在启动");
        }
    }
}

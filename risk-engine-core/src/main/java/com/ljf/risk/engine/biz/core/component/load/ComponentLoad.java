package com.ljf.risk.engine.biz.core.component.load;


import org.springframework.core.Ordered;

/**
 * 规则引擎组件加载器
 * @author lijinfeng
 */
public interface ComponentLoad extends Ordered {

    public void load();

}

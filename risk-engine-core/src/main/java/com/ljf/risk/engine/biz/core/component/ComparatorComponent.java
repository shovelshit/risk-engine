package com.ljf.risk.engine.biz.core.component;

import com.ljf.risk.engine.biz.core.SpringContextComponent;
import com.ljf.risk.engine.biz.core.comparator.Comparator;
import com.ljf.risk.engine.biz.core.component.load.AbstractComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class ComparatorComponent extends AbstractComponent<String, Comparator> {

    @Autowired
    private SpringContextComponent springContextComponent;

    @Override
    public String getComponentName() {
        return "比较器";
    }

    @Override
    public void load() {
        Map<String, Comparator> beans = springContextComponent.getBeans(Comparator.class);
        this.cache = new ConcurrentHashMap<>(beans);
    }

    public Comparator getCacheByComponentName(String name) {
        return cache.get(name);
    }

    @Override
    public int getOrder() {
        return 0;
    }


}

package com.ljf.risk.engine.biz.core.component;

import com.ljf.risk.engine.biz.core.component.load.AbstractComponent;
import com.ljf.risk.engine.biz.core.indicator.IndicatorHandle;
import com.ljf.risk.engine.biz.service.IndicatorService;
import com.ljf.risk.engine.common.entity.Condition;
import com.ljf.risk.engine.common.entity.ConditionGroup;
import com.ljf.risk.engine.common.entity.Indicator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@RestController
public class IndicatorComponent extends AbstractComponent<Long, Indicator> {

    private final IndicatorService indicatorService;
    private final IndicatorHandle indicatorHandle;

    public IndicatorComponent(IndicatorService indicatorService, IndicatorHandle indicatorHandle) {
        this.indicatorService = indicatorService;
        this.indicatorHandle = indicatorHandle;
    }

    @Override
    public String getComponentName() {
        return "指标";
    }


    @Override
    public void load() {
        List<Indicator> list = indicatorService.list();
        this.cache = list.stream().collect(Collectors.toConcurrentMap(Indicator::getId, indicator -> indicator));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public List<Indicator> getCacheByConditions(Map<Long, List<Condition>> conditionCacheByRules, Map<Long, List<ConditionGroup>> conditionGroupCacheByRules) {
        List<Indicator> indicators = new ArrayList<>();
        for (List<Condition> value : conditionCacheByRules.values()) {
            for (Condition condition : value) {
                getIndicator(indicators, condition);
            }
        }
        Collection<List<ConditionGroup>> values = conditionGroupCacheByRules.values();
        for (List<ConditionGroup> value : values) {
            for (ConditionGroup conditionGroup : value) {
                for (Condition condition : conditionGroup.getConditionList()) {
                    getIndicator(indicators, condition);
                }
            }
        }
        return indicators;
    }

    private void getIndicator(List<Indicator> indicators, Condition condition) {
        if (Objects.equals(condition.getLeftType(), Condition.PropertyType.INDICATOR)) {
            Indicator indicator = cache.get(Long.parseLong(condition.getLeftProperty()));
            if (Objects.nonNull(indicator)) {
                indicator.setAnalysisPeriod(condition.getLeftValue());
                indicators.add(indicator);
            }
        }
        if (Objects.equals(condition.getRightType(), Condition.PropertyType.INDICATOR)) {
            Indicator indicator = cache.get(Long.parseLong(condition.getRightProperty()));
            if (Objects.nonNull(indicator)) {
                indicator.setAnalysisPeriod(condition.getRightValue());
                indicators.add(indicator);
            }
        }
    }

    public void calculateIndicator(Map<Long, List<Condition>> conditionCacheByRules, Map<Long, List<ConditionGroup>> conditionGroupCacheByRules) {
        List<Indicator> cacheByConditions = getCacheByConditions(conditionCacheByRules, conditionGroupCacheByRules);
        if (CollectionUtils.isNotEmpty(cacheByConditions)) {
            analysis(cacheByConditions);
        }
    }

    public void analysis(List<Indicator> indicators) {
        indicatorHandle.analysis(indicators);
    }

    public void accumulate(List<Indicator> indicators) {
        indicatorHandle.accumulate(indicators);
    }

}

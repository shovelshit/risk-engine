package com.ljf.risk.engine.biz.core.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljf.risk.engine.biz.core.component.load.AbstractComponent;
import com.ljf.risk.engine.biz.core.constants.CurrentContext;
import com.ljf.risk.engine.biz.service.EventService;
import com.ljf.risk.engine.common.entity.Event;
import com.ljf.risk.engine.common.entity.Indicator;
import com.ljf.risk.engine.common.entity.Rule;
import com.ljf.risk.engine.common.entity.constants.CommonStatus;
import com.ljf.risk.engine.common.entity.constants.Status;
import com.ljf.risk.engine.common.entity.enums.RuleResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class EventComponent extends AbstractComponent<Long, Event> {

    private final EventService eventService;

    private final ThreadPoolTaskExecutor accumulateThreadPoolTaskExecutor;

    private final com.ljf.risk.engine.biz.core.component.IndicatorComponent indicatorComponent;
    private final com.ljf.risk.engine.biz.core.component.EventIndicatorRelationComponent eventIndicatorRelationComponent;

    public EventComponent(EventService eventService, ThreadPoolTaskExecutor accumulateThreadPoolTaskExecutor, com.ljf.risk.engine.biz.core.component.IndicatorComponent indicatorComponent, com.ljf.risk.engine.biz.core.component.EventIndicatorRelationComponent eventIndicatorRelationComponent) {
        this.eventService = eventService;
        this.accumulateThreadPoolTaskExecutor = accumulateThreadPoolTaskExecutor;
        this.indicatorComponent = indicatorComponent;
        this.eventIndicatorRelationComponent = eventIndicatorRelationComponent;
    }

    @Override
    public String getComponentName() {
        return "事件";
    }

    @Override
    public void load() {
        List<Event> list = eventService.list(new LambdaQueryWrapper<Event>().eq(Event::getStatus, Status.ONLINE));
        this.cache = list.stream().collect(Collectors.toConcurrentMap(Event::getId, event -> event));
    }

    public Event getCacheByCode(String code) {
        for (Map.Entry<Long, Event> longEventEntry : cache.entrySet()) {
            Event event = longEventEntry.getValue();
            if (Objects.equals(code, event.getCode())) {
                return event;
            }
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public void accumulate() {
        Rule hitRule = CurrentContext.currentCtx().getHitRule();
        // 无命中规则或命中通过规则才累计
        if (hitRule == null || RuleResult.pass(hitRule.getResult())) {
            Event event = CurrentContext.currentCtx().getEvent();
            List<Indicator> indicators = eventIndicatorRelationComponent.getCacheByEventId(event.getId());
            if (CommonStatus.YES.equals(event.getAccumulate()) && CollectionUtils.isNotEmpty(indicators)) {
                CurrentContext.Context context = CurrentContext.currentCtx();
                String traceId = MDC.get("traceId");
                accumulateThreadPoolTaskExecutor.execute(() -> {
                    MDC.put("traceId", traceId);
                    try (CurrentContext ignored = new CurrentContext(context)) {
                        indicatorComponent.accumulate(indicators);
                    }
                });
            }
        }
    }
}

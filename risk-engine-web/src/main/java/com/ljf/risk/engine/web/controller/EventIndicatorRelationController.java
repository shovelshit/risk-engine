package com.ljf.risk.engine.web.controller;




import com.ljf.risk.engine.biz.service.EventIndicatorRelationService;
import com.ljf.risk.engine.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lijinfeng
 */
@RestController
@RequestMapping("/engine/event-indicator-relation")
@Slf4j
public class EventIndicatorRelationController {

    private final EventIndicatorRelationService eventIndicatorRelationService;

    public EventIndicatorRelationController(EventIndicatorRelationService eventIndicatorRelationService) {
        this.eventIndicatorRelationService = eventIndicatorRelationService;
    }

    @PostMapping("relation-events")
    
    public Result relationEvents(@RequestBody @NotNull(message = "缺少必填项") List<Long> eventIds, @RequestParam("indicatorId") @NotNull(message = "缺少必填项") Long indicatorId) {
        return eventIndicatorRelationService.relationEvents(eventIds, indicatorId) ? Result.succ() : Result.fail();
    }

}

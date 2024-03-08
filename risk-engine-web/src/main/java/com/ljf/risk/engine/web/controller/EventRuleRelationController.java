package com.ljf.risk.engine.web.controller;

import com.ljf.risk.engine.biz.service.EventRuleRelationService;
import com.ljf.risk.engine.common.entity.EventRuleRelation;
import com.ljf.risk.engine.common.validation.group.DeleteGroup;
import com.ljf.risk.engine.common.validation.group.InsertGroup;
import com.ljf.risk.engine.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/engine/event-rule-relation")
@Slf4j
public class EventRuleRelationController {

    private final EventRuleRelationService eventRuleRelationService;

    public EventRuleRelationController(EventRuleRelationService eventRuleRelationService) {
        this.eventRuleRelationService = eventRuleRelationService;
    }

    @DeleteMapping("delete")

    public Result delete(@RequestBody @Validated(DeleteGroup.class) EventRuleRelation eventRuleRelation) {
        return eventRuleRelationService.delete(eventRuleRelation) ? Result.succ() : Result.fail();
    }

    @PostMapping("add")

    public Result add(@RequestBody @Validated(InsertGroup.class) EventRuleRelation eventRuleRelation) {
        return eventRuleRelationService.add(eventRuleRelation) ? Result.succ() : Result.fail();
    }

    @PostMapping("relation-events")

    public Result relationEvents(@RequestBody @NotNull(message = "缺少必填项") List<Long> eventIds, @RequestParam("ruleId") @NotNull(message = "缺少必填项") Long ruleId) {
        return eventRuleRelationService.relationEvents(eventIds, ruleId) ? Result.succ() : Result.fail();
    }

}

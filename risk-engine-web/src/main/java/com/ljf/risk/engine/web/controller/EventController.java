package com.ljf.risk.engine.web.controller;

import com.ljf.risk.engine.biz.service.EventService;
import com.ljf.risk.engine.common.entity.Event;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.request.RelationRuleReq;
import com.ljf.risk.engine.common.validation.group.DeleteGroup;
import com.ljf.risk.engine.common.validation.group.InsertGroup;
import com.ljf.risk.engine.common.validation.group.UpdateGroup;
import com.ljf.risk.engine.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijinfeng
 */
@RestController
@RequestMapping("/engine/event")
@Slf4j
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("page")
    public Result pageEvent(@RequestBody @Validated PageReq<Event> event) {
        return Result.succ().data(eventService.pageEvent(event));
    }

    @PostMapping("update")
    public Result update(@RequestBody @Validated(UpdateGroup.class) Event event) {
        return eventService.update(event) ? Result.succ() : Result.fail();
    }

    @DeleteMapping("delete")
    public Result delete(@RequestBody @Validated(DeleteGroup.class) Event event) {
        return eventService.delete(event) ? Result.succ() : Result.fail();
    }

    @PostMapping("add")
    public Result add(@RequestBody @Validated(InsertGroup.class) Event event) {
        return eventService.add(event) ? Result.succ() : Result.fail();
    }

    @PostMapping("relation-rule")
    public Result add(@RequestBody @Validated PageReq<RelationRuleReq> relationRuleReqPageReq) {
        return Result.succ().data(eventService.pageRelationRule(relationRuleReqPageReq));
    }




}

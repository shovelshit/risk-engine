package com.ljf.risk.engine.web.controller;

import com.ljf.risk.engine.biz.service.IndicatorService;
import com.ljf.risk.engine.common.entity.EventIndicatorRelation;
import com.ljf.risk.engine.common.entity.Indicator;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.Event;
import com.ljf.risk.engine.common.validation.group.DeleteGroup;
import com.ljf.risk.engine.common.validation.group.InsertGroup;
import com.ljf.risk.engine.common.validation.group.UpdateGroup;
import com.ljf.risk.engine.common.vo.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lijinfeng
 */
@RestController
@RequestMapping("/engine/indicator")
@Slf4j
public class IndicatorController {

    private final IndicatorService indicatorService;

    public IndicatorController(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    @GetMapping("list")
    public Result listData() {
        return Result.succ().data(indicatorService.list());
    }

    @PostMapping("page")
    public Result pageData(@RequestBody @Validated PageReq<Indicator> returnMessage) {
        return Result.succ().data(indicatorService.pageData(returnMessage));
    }

    @PostMapping("update")
    public Result update(@RequestBody @Validated(UpdateGroup.class) Indicator indicator) {
        return indicatorService.update(indicator) ? Result.succ() : Result.fail();
    }

    @DeleteMapping("delete")
    public Result delete(@RequestBody @Validated(DeleteGroup.class) Indicator indicator) {
        return indicatorService.delete(indicator) ? Result.succ() : Result.fail();
    }

    @PostMapping("add")
    public Result add(@RequestBody @Validated(InsertGroup.class) Indicator indicator) {
        return indicatorService.add(indicator) ? Result.succ() : Result.fail();
    }


    @GetMapping("relation-data")
    public Result relationData(@RequestParam("indicatorId") Long indicatorId) {
        return Result.succ().data(indicatorService.relationData(indicatorId));
    }

//    @PostMapping("test-indicator")
//    public Result testIndicator(@RequestBody IndicatorAdmin.TestReq testReq) {
//        return Result.succ().data(indicatorService.testIndicator(testReq));
//    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelationDataRes {
        private List<Event> events;
        private List<EventIndicatorRelation> eventIndicatorRelations;
    }

}

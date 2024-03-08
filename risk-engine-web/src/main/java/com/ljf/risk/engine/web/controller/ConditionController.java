package com.ljf.risk.engine.web.controller;

import com.ljf.risk.engine.biz.service.ConditionGroupService;
import com.ljf.risk.engine.biz.service.ConditionService;
import com.ljf.risk.engine.common.entity.constants.ConditionRelationType;
import com.ljf.risk.engine.common.entity.request.createConditionsReq;
import com.ljf.risk.engine.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author lijinfeng
 */
@RestController
@RequestMapping("/engine/condition")
@Slf4j
public class ConditionController {

    private final ConditionService conditionService;

    private final ConditionGroupService conditionGroupService;

    public ConditionController(ConditionService conditionService, ConditionGroupService conditionGroupService) {
        this.conditionService = conditionService;
        this.conditionGroupService = conditionGroupService;
    }

    @GetMapping("list")
    public Result listCondition(@RequestParam("relationId") @NotNull Long relationId, @RequestParam("relationType") @NotNull ConditionRelationType conditionRelationType) {
        return Result.succ().data(conditionService.listCondition(relationId, conditionRelationType));
    }

    @GetMapping("condition-group-list")
    public Result listConditionGroup(@RequestParam("relationId") @NotNull Long relationId, @RequestParam("relationType") @NotNull ConditionRelationType conditionRelationType) {
        return Result.succ().data(conditionGroupService.listConditionGroup(relationId, conditionRelationType));
    }

    @PostMapping("create-conditions")
    public Result createConditions(@RequestBody @Validated createConditionsReq req) {
        return conditionService.createConditions(req) ? Result.succ().msg("更新成功") : Result.fail().msg("更新失败");
    }

}

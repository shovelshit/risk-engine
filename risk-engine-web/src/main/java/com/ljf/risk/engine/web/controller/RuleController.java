package com.ljf.risk.engine.web.controller;

import com.ljf.risk.engine.biz.service.RuleService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.Rule;
import com.ljf.risk.engine.common.validation.group.DeleteGroup;
import com.ljf.risk.engine.common.validation.group.InsertGroup;
import com.ljf.risk.engine.common.validation.group.UpdateGroup;
import com.ljf.risk.engine.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/engine/rule")
@Slf4j
public class RuleController {

    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping("page")
    public Result pageEvent(@RequestBody @Validated PageReq<Rule> rulePageReq) {
        return Result.succ().data(ruleService.pageRule(rulePageReq));
    }

    @PostMapping("update")
    public Result update(@RequestBody @Validated(UpdateGroup.class) Rule rule) {
        return ruleService.update(rule) ? Result.succ() : Result.fail();
    }

    @PostMapping("update-status")
    public Result updateStatus(@RequestBody @Validated(UpdateGroup.class) Rule rule) {
        return ruleService.updateStatus(rule) ? Result.succ() : Result.fail();
    }

    @DeleteMapping("delete")
    public Result delete(@RequestBody @Validated(DeleteGroup.class) Rule rule) {
        return ruleService.delete(rule) ? Result.succ() : Result.fail();
    }

    @PostMapping("add")
    public Result add(@RequestBody @Validated(InsertGroup.class) Rule rule) {
        return ruleService.add(rule) ? Result.succ() : Result.fail();
    }


    @GetMapping("relation-data")
    public Result relationData(@RequestParam("ruleId") @Validated @NotNull(message = "缺少规则ID") Long ruleId) {
        return Result.succ().data(ruleService.relationData(ruleId));
    }

    @GetMapping("rule-all-attribute")
    public Result ruleAllAttribute(@RequestParam("ruleId") @Validated @NotNull(message = "缺少规则ID") Long ruleId) {
        return Result.succ().data(ruleService.ruleAllAttribute(ruleId));
    }

//    @PostMapping("test-rule")
//
//    public Result testRule(@RequestBody RuleAdmin.TestRuleReq testRuleReq) {
//        return Result.succ().data(ruleService.testRule(testRuleReq));
//    }


}

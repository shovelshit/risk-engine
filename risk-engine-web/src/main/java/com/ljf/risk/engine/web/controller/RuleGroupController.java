package com.ljf.risk.engine.web.controller;

import com.ljf.risk.engine.biz.service.RuleGroupService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.RuleGroup;
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
@RequestMapping("/engine/rule-group")
@Slf4j
public class RuleGroupController {

    private final RuleGroupService ruleGroupService;

    public RuleGroupController(RuleGroupService ruleGroupService) {
        this.ruleGroupService = ruleGroupService;
    }

    @PostMapping("page")

    public Result pageEvent(@RequestBody @Validated PageReq<RuleGroup> ruleGroupPageReq) {
        return Result.succ().data(ruleGroupService.pageRuleGroup(ruleGroupPageReq));
    }

    @PostMapping("update")

    public Result update(@RequestBody @Validated(UpdateGroup.class) RuleGroup ruleGroup) {
        return ruleGroupService.update(ruleGroup) ? Result.succ() : Result.fail();
    }

    @DeleteMapping("delete")

    public Result delete(@RequestBody @Validated(DeleteGroup.class) RuleGroup ruleGroup) {
        return ruleGroupService.delete(ruleGroup) ? Result.succ() : Result.fail();
    }

    @PostMapping("add")

    public Result add(@RequestBody @Validated(InsertGroup.class) RuleGroup ruleGroup) {
        return ruleGroupService.add(ruleGroup) ? Result.succ() : Result.fail();
    }

}

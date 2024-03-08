package com.ljf.risk.engine.web.controller;

import com.ljf.risk.engine.biz.service.FunctionExtentService;
import com.ljf.risk.engine.common.entity.FunctionExtend;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.validation.group.DeleteGroup;
import com.ljf.risk.engine.common.validation.group.InsertGroup;
import com.ljf.risk.engine.common.validation.group.UpdateGroup;
import com.ljf.risk.engine.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijinfeng
 */
@RestController
@RequestMapping("/engine/function-extent")
@Slf4j
public class FunctionExtentController {

    @Autowired
    private FunctionExtentService functionExtentService;

    @GetMapping("list")
    public Result listData() {
        return Result.succ().data(functionExtentService.list());
    }

    @PostMapping("page")
    public Result pageData(@RequestBody @Validated PageReq<FunctionExtend> pageReq) {
        return Result.succ().data(functionExtentService.pageFunction(pageReq));
    }

    @PostMapping("update")
    public Result update(@RequestBody @Validated(UpdateGroup.class) FunctionExtend functionExtend) {
        return functionExtentService.update(functionExtend) ? Result.succ() : Result.fail();
    }

    @DeleteMapping("delete")
    public Result delete(@RequestBody @Validated(DeleteGroup.class) FunctionExtend functionExtend) {
        return functionExtentService.delete(functionExtend) ? Result.succ() : Result.fail();
    }

    @PostMapping("add")
    public Result add(@RequestBody @Validated(InsertGroup.class) FunctionExtend functionExtend) {
        return functionExtentService.add(functionExtend) ? Result.succ() : Result.fail();
    }

}

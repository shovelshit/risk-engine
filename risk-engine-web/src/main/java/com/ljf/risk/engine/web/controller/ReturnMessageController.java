package com.ljf.risk.engine.web.controller;


import com.ljf.risk.engine.biz.service.ReturnMessageService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.ReturnMessage;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijinfeng
 */
@RestController
@RequestMapping("/engine/return-message")
@Slf4j
public class ReturnMessageController {

    private final ReturnMessageService returnMessageService;

    public ReturnMessageController(ReturnMessageService returnMessageService) {
        this.returnMessageService = returnMessageService;
    }

    @GetMapping("list")
    
    public Result listData() {
        return Result.succ().data(returnMessageService.list());
    }

    @PostMapping("page")
    
    public Result pageData(@RequestBody @Validated PageReq<ReturnMessage> returnMessage) {
        return Result.succ().data(returnMessageService.pageReturnCode(returnMessage));
    }

    @PostMapping("update")
    
    public Result update(@RequestBody @Validated(UpdateGroup.class) ReturnMessage returnMessage) {
        return returnMessageService.update(returnMessage) ? Result.succ() : Result.fail();
    }

    @DeleteMapping("delete")
    
    public Result delete(@RequestBody @Validated(DeleteGroup.class) ReturnMessage returnMessage) {
        return returnMessageService.delete(returnMessage) ? Result.succ() : Result.fail();
    }

    @PostMapping("add")
    
    public Result add(@RequestBody @Validated(InsertGroup.class) ReturnMessage returnMessage) {
        return returnMessageService.add(returnMessage) ? Result.succ() : Result.fail();
    }

}

package com.ljf.risk.engine.web.controller;


import com.ljf.risk.engine.biz.service.FunctionService;
import com.ljf.risk.engine.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijinfeng
 */
@RestController
@RequestMapping("/engine/function")
@Slf4j
public class FunctionController {

    @Autowired
    private FunctionService functionService;

    @GetMapping("list")

    public Result listData() {
        return Result.succ().data(functionService.list());
    }

}

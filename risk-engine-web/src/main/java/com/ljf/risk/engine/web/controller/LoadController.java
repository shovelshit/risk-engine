package com.ljf.risk.engine.web.controller;


import com.ljf.risk.engine.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lijinfeng
 */
@RestController
@RequestMapping("/engine/load")
@Slf4j
public class LoadController {

    @GetMapping()
    
    public Result load() {
        return true ? Result.succ() : Result.fail();
    }


}

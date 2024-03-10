package com.ljf.risk.engine.web.controller;

import com.ljf.risk.engine.biz.core.component.InitComponent;
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
@RequestMapping("/engine/load")
@Slf4j
public class LoadController {

    @Autowired
    private InitComponent initComponent;

    @GetMapping()
    public Result load() {
        return initComponent.load() ? Result.succ() : Result.fail();
    }


}

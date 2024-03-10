package com.ljf.risk.engine.web.controller;

import com.ljf.risk.engine.biz.core.component.RiskControlComponent;
import com.ljf.risk.engine.common.entity.EngineCheckReq;
import com.ljf.risk.engine.common.entity.EngineCheckRes;
import com.ljf.risk.engine.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijinfeng
 */
@Slf4j
@RequestMapping("/engine")
@RestController
public class RiskController {

    private final RiskControlComponent riskControlComponent;

    public RiskController(RiskControlComponent riskControlComponent) {
        this.riskControlComponent = riskControlComponent;
    }

    @PostMapping("check")
    public EngineCheckRes check(@RequestBody EngineCheckReq engineCheckReq) {
        return riskControlComponent.check(engineCheckReq);
    }

}

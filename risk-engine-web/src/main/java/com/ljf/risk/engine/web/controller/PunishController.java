package com.ljf.risk.engine.web.controller;

import com.ljf.risk.engine.biz.service.PunishService;
import com.ljf.risk.engine.common.entity.Punish;
import com.ljf.risk.engine.common.validation.group.DeleteGroup;
import com.ljf.risk.engine.common.validation.group.InsertGroup;
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
@RequestMapping("/engine/punish")
@Slf4j
public class PunishController {

    private final PunishService punishService;

    public PunishController(PunishService punishService) {
        this.punishService = punishService;
    }

    @DeleteMapping("delete")
    
    public Result delete(@RequestBody @Validated(DeleteGroup.class) Punish punish) {
        return punishService.delete(punish) ? Result.succ() : Result.fail();
    }

    @PostMapping("add")
    
    public Result add(@RequestBody @Validated(InsertGroup.class) Punish punish) {
        return punishService.add(punish) ? Result.succ() : Result.fail();
    }

}

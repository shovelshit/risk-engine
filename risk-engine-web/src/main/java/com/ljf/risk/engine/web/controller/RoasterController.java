package com.ljf.risk.engine.web.controller;

import com.google.common.collect.Lists;
import com.ljf.risk.engine.biz.service.RoasterService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.Roaster;
import com.ljf.risk.engine.common.validation.group.DeleteGroup;
import com.ljf.risk.engine.common.validation.group.InsertGroup;
import com.ljf.risk.engine.common.validation.group.UpdateGroup;
import com.ljf.risk.engine.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/engine/roaster")
@Slf4j
public class RoasterController {

    private final RoasterService roasterService;

    public RoasterController(RoasterService roasterService) {
        this.roasterService = roasterService;
    }

    @PostMapping("page")
    public Result pageEvent(@RequestBody @Validated PageReq<Roaster> roasterModelPageReq) {
        return Result.succ().data(roasterService.pageData(roasterModelPageReq));
    }

    @GetMapping("list")
    public Result list() {
        return Result.succ().data(roasterService.list());
    }

    @PostMapping("update")
    public Result update(@RequestBody @Validated(UpdateGroup.class) Roaster roasterModel) {
        Roaster model = new Roaster();
        BeanUtils.copyProperties(roasterModel, model);
        return roasterService.createOrUpdate(model) ? Result.succ() : Result.fail();
    }

    @DeleteMapping("delete")
    public Result delete(@RequestBody @Validated(DeleteGroup.class) Roaster roasterModel) {
        return roasterService.delete(Lists.newArrayList(roasterModel.getId())) ? Result.succ() : Result.fail();
    }

    @PostMapping("add")
    public Result add(@RequestBody @Validated(InsertGroup.class) Roaster roasterModel) {
        Roaster model = new Roaster();
        BeanUtils.copyProperties(roasterModel, model);
        return roasterService.createOrUpdate(model) ? Result.succ() : Result.fail();
    }

}

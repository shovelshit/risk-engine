package com.ljf.risk.engine.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijinfeng
 */
@RestController
@RequestMapping("/engine/roaster")
@Slf4j
public class RoasterController {

//    private final RoasterService roasterService;

//    private final RoasterAdmin roasterAdmin;

//    public RoasterController(RoasterService roasterService, RoasterAdmin roasterAdmin) {
//        this.roasterService = roasterService;
//        this.roasterAdmin = roasterAdmin;
//    }
//
//    @PostMapping("page")
//
//    public Result pageEvent(@RequestBody @Validated PageReq<RoasterModel> roasterModelPageReq) {
//        return Result.succ().data(roasterService.pageData(roasterModelPageReq));
//    }
//
//    @GetMapping("list")
//
//    public Result list() {
//        return Result.succ().data(roasterService.list());
//    }
//
//    @PostMapping("update")
//
//    public Result update(@RequestBody @Validated(UpdateGroup.class) RoasterModel roasterModel) {
//        RoasterModel model = new RoasterModel();
//        BeanUtils.copyProperties(roasterModel, model);
//        return roasterAdmin.createOrUpdate(model).isSuccess() ? Result.succ() : Result.fail();
//    }
//
//    @DeleteMapping("delete")
//
//    public Result delete(@RequestBody @Validated(DeleteGroup.class) RoasterModel roasterModel) {
//        return roasterAdmin.delete(Lists.list(roasterModel.getId())).isSuccess() ? Result.succ() : Result.fail();
//    }
//
//    @PostMapping("add")
//
//    public Result add(@RequestBody @Validated(InsertGroup.class) RoasterModel roasterModel) {
//        RoasterModel model = new RoasterModel();
//        BeanUtils.copyProperties(roasterModel, model);
//        return roasterAdmin.createOrUpdate(model).isSuccess() ? Result.succ() : Result.fail();
//    }

}

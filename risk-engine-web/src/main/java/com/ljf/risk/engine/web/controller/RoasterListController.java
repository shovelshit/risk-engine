//package com.ljf.risk.engine.web.controller;
//
//import com.alibaba.excel.EasyExcel;
//
//
//
//
//
//
//
//
//
//
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.util.Lists;
//import org.springframework.beans.BeanUtils;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.constraints.NotBlank;
//import java.io.IOException;
//import java.util.Date;
//
///**
// * @author lijinfeng
// */
//@RestController
//@RequestMapping("/engine/roaster-list")
//@Slf4j
//public class RoasterListController {
//
//    private final RoasterListService roasterListService;
//
//    private final RoasterAdmin roasterAdmin;
//
//    public RoasterListController(RoasterListService roasterListService, RoasterAdmin roasterAdmin) {
//        this.roasterListService = roasterListService;
//        this.roasterAdmin = roasterAdmin;
//    }
//
//    @PostMapping("page")
//
//    public Json pageEvent(@RequestBody @Validated PageReq<RoasterListModel> roasterListModelPageReq) {
//        return Json.succ().data(roasterListService.pageData(roasterListModelPageReq));
//    }
//
//    @PostMapping("update")
//
//    public Json update(@RequestBody @Validated(UpdateGroup.class) RoasterListModel roasterListModel) {
//        com.wfkj.hobby.hawkeye.engine.api.roaster.RoasterListModel model = new com.wfkj.hobby.hawkeye.engine.api.roaster.RoasterListModel();
//        BeanUtils.copyProperties(roasterListModel, model);
//        model.setUpdateUser(ThreadLocalUtils.getUsername());
//        Response response = roasterAdmin.update(model, roasterListModel.getRoasterId());
//        return response.isSuccess() ? Json.succ().msg(response.getMsg()) : Json.fail().msg(response.getMsg());
//    }
//
//    @DeleteMapping("delete")
//
//    public Json delete(@RequestBody @Validated(DeleteGroup.class) RoasterListModel roasterListModel) {
//        Response response = roasterAdmin.deleteLists(roasterListModel.getRoasterId(), Lists.list(roasterListModel.getId()));
//        return response.isSuccess() ? Json.succ().msg(response.getMsg()) : Json.fail().msg(response.getMsg());
//    }
//
//    @PostMapping("add")
//
//    public Json add(@RequestBody @Validated(InsertGroup.class) RoasterListModel roasterListModel) {
//        com.wfkj.hobby.hawkeye.engine.api.roaster.RoasterListModel model = new com.wfkj.hobby.hawkeye.engine.api.roaster.RoasterListModel();
//        BeanUtils.copyProperties(roasterListModel, model);
//        Date date = new Date();
//        model.setCreateTime(date);
//        model.setUpdateUser(ThreadLocalUtils.getUsername());
//        Response response = roasterAdmin.addRoasterList(roasterListModel.getRoasterId(), Lists.list(model));
//        return response.isSuccess() ? Json.succ().msg(response.getMsg()) : Json.fail().msg(response.getMsg());
//    }
//
//    @PostMapping("import/{roasterId}/{expiredAt}")
//
//    public Json importData(@RequestParam("file") MultipartFile file, @PathVariable("roasterId") @NotBlank Long roasterId, @PathVariable("expiredAt") @NotBlank Long expiredAt, HttpServletResponse response) throws IOException {
//        RoasterListListener roasterListListener = new RoasterListListener(roasterId, ThreadLocalUtils.getUsername(), roasterListService, new Date(expiredAt));
//        roasterListListener.continueAfterThrowing(true);
//        EasyExcel.read(file.getInputStream(), RoasterListModel.class, roasterListListener).sheet().doRead();
//        String msg = String.format("总量: %s, 成功: %s", roasterListListener.getTotal(), roasterListListener.getSuccessCount());
//        if (roasterListListener.getErrorRowSize() > 0) {
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//            response.setCharacterEncoding("utf-8");
//            EasyExcel.write(response.getOutputStream(), RoasterListModel.class).sheet().doWrite(roasterListListener.getErrRowsList());
//            return null;
//        }
//        return roasterListListener.getErrorRowSize() == 0 ? Json.succ().msg(msg) : Json.fail().msg(msg);
//    }
//
//}

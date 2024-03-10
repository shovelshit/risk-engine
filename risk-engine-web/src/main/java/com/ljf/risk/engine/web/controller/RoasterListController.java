package com.ljf.risk.engine.web.controller;

import com.alibaba.excel.EasyExcel;
import com.google.common.collect.Lists;
import com.ljf.risk.engine.biz.service.RoasterListService;
import com.ljf.risk.engine.biz.service.RoasterService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.RoasterList;
import com.ljf.risk.engine.common.utils.ThreadLocalUtils;
import com.ljf.risk.engine.common.validation.group.DeleteGroup;
import com.ljf.risk.engine.common.validation.group.InsertGroup;
import com.ljf.risk.engine.common.validation.group.UpdateGroup;
import com.ljf.risk.engine.common.vo.Result;
import com.ljf.risk.engine.web.listener.RoasterListListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Date;

/**
 * @author lijinfeng
 */
@RestController
@RequestMapping("/engine/roaster-list")
@Slf4j
public class RoasterListController {

    private final RoasterListService roasterListService;    
    
    private final RoasterService roasterService;

    public RoasterListController(RoasterListService roasterListService, RoasterService roasterService) {
        this.roasterListService = roasterListService;
        this.roasterService = roasterService;
    }

    @PostMapping("page")
    public Result pageEvent(@RequestBody @Validated PageReq<RoasterList> roasterListModelPageReq) {
        return Result.succ().data(roasterListService.pageData(roasterListModelPageReq));
    }

    @PostMapping("update")
    public Result update(@RequestBody @Validated(UpdateGroup.class) RoasterList roasterListModel) {
        RoasterList model = new RoasterList();
        BeanUtils.copyProperties(roasterListModel, model);
        model.setUpdateUser(ThreadLocalUtils.getUsername());
        return roasterService.update(model, roasterListModel.getRoasterId()) ? Result.succ() : Result.fail();
    }

    @DeleteMapping("delete")
    public Result delete(@RequestBody @Validated(DeleteGroup.class) RoasterList roasterListModel) {
        return roasterService.deleteLists(roasterListModel.getRoasterId(), Lists.newArrayList(roasterListModel.getId())) ? Result.succ() : Result.fail();
    }

    @PostMapping("add")
    public Result add(@RequestBody @Validated(InsertGroup.class) RoasterList roasterListModel) {
        RoasterList model = new RoasterList();
        BeanUtils.copyProperties(roasterListModel, model);
        Date date = new Date();
        model.setCreateTime(date);
        model.setUpdateUser(ThreadLocalUtils.getUsername());
        return roasterService.addRoasterList(roasterListModel.getRoasterId(), Lists.newArrayList(model)) ? Result.succ() : Result.fail();
    }

    @PostMapping("import/{roasterId}/{expiredAt}")
    public Result importData(@RequestParam("file") MultipartFile file, @PathVariable("roasterId") @NotBlank Long roasterId, @PathVariable("expiredAt") @NotBlank Long expiredAt, HttpServletResponse response) throws IOException {
        RoasterListListener roasterListListener = new RoasterListListener(roasterId, ThreadLocalUtils.getUsername(), roasterListService, new Date(expiredAt));
        roasterListListener.continueAfterThrowing(true);
        EasyExcel.read(file.getInputStream(), RoasterList.class, roasterListListener).sheet().doRead();
        String msg = String.format("总量: %s, 成功: %s", roasterListListener.getTotal(), roasterListListener.getSuccessCount());
        if (roasterListListener.getErrorRowSize() > 0) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            EasyExcel.write(response.getOutputStream(), RoasterList.class).sheet().doWrite(roasterListListener.getErrRowsList());
            return null;
        }
        return roasterListListener.getErrorRowSize() == 0 ? Result.succ().msg(msg) : Result.fail().msg(msg);
    }

}

package com.ljf.risk.engine.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.ReturnMessage;

public interface ReturnMessageService extends IService<ReturnMessage> {
    IPage<ReturnMessage> pageReturnCode(PageReq<ReturnMessage> returnMessagePageReq);

    boolean update(ReturnMessage returnMessage);

    boolean delete(ReturnMessage returnMessage);

    boolean add(ReturnMessage returnMessage);
}

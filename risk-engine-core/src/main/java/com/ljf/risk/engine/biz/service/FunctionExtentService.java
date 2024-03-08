package com.ljf.risk.engine.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.FunctionExtend;
import com.ljf.risk.engine.common.entity.PageReq;

/**
 * @author lijinfeng
 */
public interface FunctionExtentService extends IService<FunctionExtend> {
    IPage<FunctionExtend> pageFunction(PageReq<FunctionExtend> pageReq);

    boolean update(FunctionExtend functionExtend);

    boolean delete(FunctionExtend functionExtend);

    boolean add(FunctionExtend functionExtend);
}

package com.ljf.risk.engine.biz.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.Roaster;

public interface RoasterService extends IService<Roaster> {
    IPage<Roaster> pageData(PageReq<Roaster> roasterModelPageReq);

    boolean update(Roaster roaster);

    boolean delete(Roaster roaster);

    boolean add(Roaster roaster);
}

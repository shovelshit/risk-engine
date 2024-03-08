package com.ljf.risk.engine.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.RoasterList;

public interface RoasterListService extends IService<RoasterList> {
    IPage<RoasterList> pageData(PageReq<RoasterList> roasterListModelPageReq);

    boolean update(RoasterList roasterList);

    boolean delete(RoasterList roasterList);

    boolean add(RoasterList roasterList);
}

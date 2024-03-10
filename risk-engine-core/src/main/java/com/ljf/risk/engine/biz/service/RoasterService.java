package com.ljf.risk.engine.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.Roaster;
import com.ljf.risk.engine.common.entity.RoasterList;

import java.util.List;

public interface RoasterService extends IService<Roaster> {
    IPage<Roaster> pageData(PageReq<Roaster> roasterModelPageReq);

    boolean update(Roaster roaster);

    boolean delete(Roaster roaster);

    boolean add(Roaster roaster);

    boolean addRoasterList(Long roasterId, List<RoasterList> roasterListModel);

    /**
     * 名单删除
     */
    boolean deleteLists(Long roasterId, List<Long> roasterListModel);

    /**
     * 创建或更新名单，名单code需要唯一。否则报错
     */
    boolean createOrUpdate(Roaster roasterModel);

    boolean delete(List<Long> roasters);

    boolean update(RoasterList roasterListModel, Long roasterId);
}

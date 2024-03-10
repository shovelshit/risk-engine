package com.ljf.risk.engine.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.Indicator;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.request.TestReq;
import com.ljf.risk.engine.common.entity.response.RelationDataRes;

/**
 * @author lijinfeng
 */
public interface IndicatorService extends IService<Indicator> {
    IPage<Indicator> pageData(PageReq<Indicator> indicatorPageReq);

    boolean update(Indicator indicator);

    boolean delete(Indicator indicator);

    boolean add(Indicator indicator);

    RelationDataRes relationData(Long indicatorId);

    Object testIndicator(TestReq testReq);
}

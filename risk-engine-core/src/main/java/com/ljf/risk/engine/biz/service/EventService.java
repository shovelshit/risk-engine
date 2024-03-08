package com.ljf.risk.engine.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.Event;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.request.RelationRuleReq;
import com.ljf.risk.engine.common.entity.response.RelationRuleRes;

/**
 * @author lijinfeng
 */
public interface EventService extends IService<Event> {
    IPage<Event> pageEvent(PageReq<Event> event);

    boolean update(Event event);

    boolean delete(Event event);

    boolean add(Event event);

    RelationRuleRes pageRelationRule(PageReq<RelationRuleReq> relationRuleReqPageReq);
}

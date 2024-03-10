package com.ljf.risk.engine.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.Rule;
import com.ljf.risk.engine.common.entity.request.TestRuleReq;
import com.ljf.risk.engine.common.entity.response.RelationDataRes;
import com.ljf.risk.engine.common.entity.response.TestResponse;

import java.util.Set;

/**
 * @author lijinfeng
 */
public interface RuleService extends IService<Rule> {

    IPage<Rule> pageRule(PageReq<Rule> rulePageReq);

    boolean update(Rule rule);

    boolean delete(Rule rule);

    boolean add(Rule rule);

    RelationDataRes relationData(Long ruleId);

    Set<String> ruleAllAttribute(Long ruleId);

    TestResponse testRule(TestRuleReq testRuleReq);

    boolean updateStatus(Rule rule);
}

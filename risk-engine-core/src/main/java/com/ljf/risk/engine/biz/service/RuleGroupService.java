package com.ljf.risk.engine.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.RuleGroup;

/**
 * @author lijinfeng
 */
public interface RuleGroupService extends IService<RuleGroup> {
    IPage<RuleGroup> pageRuleGroup(PageReq<RuleGroup> ruleGroup);

    boolean update(RuleGroup ruleGroup);

    boolean delete(RuleGroup ruleGroup);

    boolean add(RuleGroup ruleGroup);

}

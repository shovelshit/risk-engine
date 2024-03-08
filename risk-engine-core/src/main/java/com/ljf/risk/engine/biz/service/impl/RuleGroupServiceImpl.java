package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.dao.RuleGroupDao;
import com.ljf.risk.engine.biz.service.RuleGroupService;
import com.ljf.risk.engine.biz.service.RuleService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.Rule;
import com.ljf.risk.engine.common.entity.RuleGroup;
import com.ljf.risk.engine.common.utils.ThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author lijinfeng
 */
@Service
public class RuleGroupServiceImpl extends ServiceImpl<RuleGroupDao, RuleGroup> implements RuleGroupService {
    @Autowired
    private RuleService ruleService;

    @Override
    public IPage<RuleGroup> pageRuleGroup(PageReq<RuleGroup> ruleGroup) {
        QueryWrapper<RuleGroup> qw = new QueryWrapper<>();
        if (StringUtils.isNotBlank(ruleGroup.getData().getCode())) {
            qw.like("code", ruleGroup.getData().getCode());
        }
        if (!Objects.isNull(ruleGroup.getData().getStatus())) {
            qw.eq("status", ruleGroup.getData().getStatus());
        }
        qw.orderByDesc("update_time");
        return page(new Page<>(ruleGroup.getCurrent(), ruleGroup.getSize()), qw);
    }

    @Override
    public boolean update(RuleGroup ruleGroup) {
        ruleGroup.setUpdateBy(ThreadLocalUtils.getUsername());
        return updateById(ruleGroup);
    }

    @Override
    public boolean delete(RuleGroup ruleGroup) {
        long count1 = ruleService.count(new LambdaQueryWrapper<Rule>().eq(Rule::getRuleGroupId, ruleGroup.getId()));
        if (count1 > 0) {
            throw new RuntimeException("无法删除: 已关联规则");
        }
        return removeById(ruleGroup);
    }

    @Override
    public boolean add(RuleGroup ruleGroup) {
        boolean exists = baseMapper.exists(new QueryWrapper<RuleGroup>().eq("code", ruleGroup.getCode()));
        if (exists) {
            throw new RuntimeException("该规则组已存在");
        }
        String username = ThreadLocalUtils.getUsername();
        ruleGroup.setCreateBy(username);
        return save(ruleGroup);
    }
}



package com.ljf.risk.engine.common.entity.request;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ljf.risk.engine.common.entity.RuleGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelationRuleGroupRes {
    private IPage<RuleGroup> ruleGroupPage;
    private List<Long> ruleGroupIds;
}
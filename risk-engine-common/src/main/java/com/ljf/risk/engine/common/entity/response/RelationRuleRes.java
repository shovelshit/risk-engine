package com.ljf.risk.engine.common.entity.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ljf.risk.engine.common.entity.Rule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelationRuleRes {
    private IPage<Rule> rulePage;
    private List<Long> ruleIds;
}
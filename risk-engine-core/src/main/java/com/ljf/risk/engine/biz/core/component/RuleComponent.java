package com.ljf.risk.engine.biz.core.component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljf.risk.engine.biz.core.CheckOutsideResult;
import com.ljf.risk.engine.biz.core.SpringContextComponent;
import com.ljf.risk.engine.biz.core.component.load.AbstractComponent;
import com.ljf.risk.engine.biz.core.constants.CurrentContext;
import com.ljf.risk.engine.biz.service.RuleService;
import com.ljf.risk.engine.common.entity.CheckDetails;
import com.ljf.risk.engine.common.entity.CheckResult;
import com.ljf.risk.engine.common.entity.Condition;
import com.ljf.risk.engine.common.entity.ConditionGroup;
import com.ljf.risk.engine.common.entity.EngineCheckRes;
import com.ljf.risk.engine.common.entity.ReturnMessage;
import com.ljf.risk.engine.common.entity.Rule;
import com.ljf.risk.engine.common.entity.constants.ConditionRelationType;
import com.ljf.risk.engine.common.entity.enums.RuleResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class RuleComponent extends AbstractComponent<Long, Rule> {

    private final RuleService ruleService;

    private final ConditionComponent conditionComponent;

    private final ConditionGroupComponent conditionGroupComponent;

    private final ReturnMessageComponent returnMessageComponent;

    private final FunctionExtentComponent functionExtentComponent;

    private final PunishComponent punishComponent;

    private final IndicatorComponent indicatorComponent;
    private final SpringContextComponent springContextComponent;

    public RuleComponent(RuleService ruleService, ConditionComponent conditionComponent, ConditionGroupComponent conditionGroupComponent, ReturnMessageComponent returnMessageComponent, FunctionExtentComponent functionExtentComponent, PunishComponent punishComponent, IndicatorComponent indicatorComponent, SpringContextComponent springContextComponent) {
        this.ruleService = ruleService;
        this.conditionComponent = conditionComponent;
        this.conditionGroupComponent = conditionGroupComponent;
        this.returnMessageComponent = returnMessageComponent;
        this.functionExtentComponent = functionExtentComponent;
        this.punishComponent = punishComponent;
        this.indicatorComponent = indicatorComponent;
        this.springContextComponent = springContextComponent;
    }

    @Override
    public String getComponentName() {
        return "规则";
    }

    @Override
    public void load() {
        List<Rule> list = ruleService.list(new LambdaQueryWrapper<Rule>().eq(Rule::getStatus, Rule.Status.ONLINE));
        this.cache = list.stream().collect(Collectors.toConcurrentMap(Rule::getId, rule -> rule));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public EngineCheckRes analyse(List<Rule> rules) {
        beforeAnalyse(rules);
        List<Rule> hitRules = new ArrayList<>();
        List<String> hitRuleCodes = new ArrayList<>();
        for (Rule rule : rules) {
            Boolean hit = handleCondition(rule);
            if (hit) {
                hitRules.add(rule);
                hitRuleCodes.add(rule.getCode());
            }
        }
        EngineCheckRes checkRes = EngineCheckRes.success();
        CheckResult.CheckResultBuilder checkResultBuilder = CheckResult.builder().code(CheckOutsideResult.PASS.getCode()).msg(CheckOutsideResult.PASS.getMsg());
        CheckDetails.CheckDetailsBuilder checkDetailsBuilder = CheckDetails.builder();
        checkResultBuilder.eventCode(CurrentContext.currentCtx().getEventCode()).eventCodeDesc(CurrentContext.currentCtx().getEventCodeDesc());
        if (CollectionUtils.isNotEmpty(hitRules)) {
            CurrentContext.currentCtx().setHitRuleNames(hitRuleCodes);
            hitRules.stream().max(Comparator.comparingInt(Rule::getPriority)).ifPresent(rule -> {
                CurrentContext.currentCtx().setHitRule(rule);
            });
            Rule hitRule = CurrentContext.currentCtx().getHitRule();
            punishComponent.punish(hitRule.getId());
            checkDetailsBuilder.hitRules(hitRuleCodes).hitRule(hitRule.getCode());
            checkDetailsBuilder.conditionResult(CurrentContext.currentCtx().getConditionResult());
            if (RuleResult.reject(hitRule.getResult())) {
                ReturnMessage returnMessage = returnMessageComponent.getCacheByReturnMessageId(hitRule.getReturnMessageId());
                checkResultBuilder.code(CheckOutsideResult.REJECT.getCode()).msg(CheckOutsideResult.REJECT.getMsg());
                if (returnMessage != null) {
                    checkDetailsBuilder.failedCode(returnMessage.getCode());
                    checkDetailsBuilder.failedMsg(returnMessage.getReturnMessage());
                }
                return checkRes.checkResult(checkResultBuilder.details(checkDetailsBuilder.build()).build());
            } else if (RuleResult.pass(hitRule.getResult())) {
                checkResultBuilder.code(CheckOutsideResult.PASS.getCode()).msg(CheckOutsideResult.PASS.getMsg());
                return checkRes.checkResult(checkResultBuilder.details(checkDetailsBuilder.build()).build());
            }
        }
        return checkRes.checkResult(checkResultBuilder.details(checkDetailsBuilder.build()).build());
    }

    private void beforeAnalyse(List<Rule> rules) {
        Map<Long, List<Condition>> conditionCacheByRules = conditionComponent.getCacheByRules(rules);
        Map<Long, List<ConditionGroup>> conditionGroupCacheByRules = conditionGroupComponent.getCacheByRules(rules);
        functionExtentComponent.calculateFunction(conditionCacheByRules, conditionGroupCacheByRules);
        indicatorComponent.calculateIndicator(conditionCacheByRules, conditionGroupCacheByRules);
    }

    private Boolean handleCondition(Rule rule) {
        CurrentContext.currentCtx().setConditionResult(new HashMap<>());
        Boolean conditionHit = conditionComponent.conditionHandle(rule.getId(), ConditionRelationType.RULE, rule.getLogic(), CurrentContext.currentCtx());
        Boolean conditionGroupHit = conditionGroupComponent.conditionGroupHandle(rule.getId(), ConditionRelationType.RULE, rule.getLogic(), CurrentContext.currentCtx());
        return conditionComponent.getConditionResult(conditionHit, conditionGroupHit, rule.getLogic(), ConditionRelationType.RULE);
    }

}

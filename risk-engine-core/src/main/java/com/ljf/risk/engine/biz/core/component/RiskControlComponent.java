package com.ljf.risk.engine.biz.core.component;

import com.ljf.risk.engine.biz.core.CheckOutsideResult;
import com.ljf.risk.engine.biz.core.constants.CurrentContext;
import com.ljf.risk.engine.common.entity.CheckDetails;
import com.ljf.risk.engine.common.entity.CheckResult;
import com.ljf.risk.engine.common.entity.EngineCheckReq;
import com.ljf.risk.engine.common.entity.EngineCheckRes;
import com.ljf.risk.engine.common.entity.Event;
import com.ljf.risk.engine.common.entity.Rule;
import com.ljf.risk.engine.common.entity.constants.CommonStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class RiskControlComponent {

    private final EventComponent eventComponent;
    private final EventRuleRelationComponent eventRuleRelationComponent;
    private final RuleComponent ruleComponent;

    public RiskControlComponent(EventComponent eventComponent, EventRuleRelationComponent eventRuleRelationComponent, RuleComponent ruleComponent, InitComponent initComponent) {
        this.eventComponent = eventComponent;
        this.eventRuleRelationComponent = eventRuleRelationComponent;
        this.ruleComponent = ruleComponent;
    }

    public EngineCheckRes check(EngineCheckReq engineCheckReq) {
        String eventCode = engineCheckReq.getEventCode();
        EngineCheckRes checkRes = EngineCheckRes.success();
        CheckDetails.CheckDetailsBuilder checkDetailsBuilder = CheckDetails.builder();
        CheckResult.CheckResultBuilder checkResultBuilder = CheckResult.builder().eventCode(eventCode).code(CheckOutsideResult.PASS.getCode()).msg(CheckOutsideResult.PASS.getMsg());
        checkRes.checkResult(checkResultBuilder.build());
        Date eventTime = new Date();
        try {
            Event event = eventComponent.getCacheByCode(eventCode);
            if (Objects.isNull(event)) {
                log.warn("event is empty, checkReq: {}", engineCheckReq);
                CheckDetails checkDetails = checkDetailsBuilder.failedCode(com.ljf.risk.engine.core.CheckInsideResult.NO_EVENT.getCode()).failedMsg(com.ljf.risk.engine.core.CheckInsideResult.NO_EVENT.getMsg()).build();
                CheckResult checkResult = checkResultBuilder.details(checkDetails).build();
                return checkRes.checkResult(checkResult);
            }
            checkResultBuilder.eventCodeDesc(event.getDescription());

            if (!CommonStatus.success(event.getAnalysis()) && !CommonStatus.success(event.getAccumulate())) {
                log.warn("event do not need to be analyzed and accumulate, {}", event);
                CheckDetails checkDetails = checkDetailsBuilder.failedCode(com.ljf.risk.engine.core.CheckInsideResult.EVENT_OFFLINE.getCode()).failedMsg(com.ljf.risk.engine.core.CheckInsideResult.EVENT_OFFLINE.getMsg()).build();
                CheckResult checkResult = checkResultBuilder.details(checkDetails).build();
                return checkRes.checkResult(checkResult);
            }

            try (CurrentContext ignored = new CurrentContext(CurrentContext.Context.builder().eventTime(eventTime).event(event).engineCheckReq(engineCheckReq).build())) {
                List<Rule> rules = eventRuleRelationComponent.getCacheByEventId(event.getId());
                if (CollectionUtils.isEmpty(rules)) {
                    log.warn("no rule configuration, checkReq: {}", engineCheckReq);
                    CheckDetails checkDetails = checkDetailsBuilder.failedCode(com.ljf.risk.engine.core.CheckInsideResult.NO_RULE.getCode()).failedMsg(com.ljf.risk.engine.core.CheckInsideResult.NO_RULE.getMsg()).build();
                    CheckResult checkResult = checkResultBuilder.details(checkDetails).build();
                    eventComponent.accumulate();
                    return checkRes.checkResult(checkResult);
                }
                if (CommonStatus.success(event.getAnalysis())) {
                    checkRes = ruleComponent.analyse(rules);
                }
                eventComponent.accumulate();
                return checkRes;
            }
        } catch (Exception e) {
            log.error("engine check failed: ", e);
            CheckDetails checkDetails = checkDetailsBuilder.failedCode(com.ljf.risk.engine.core.CheckInsideResult.ERROR.getCode()).failedMsg(com.ljf.risk.engine.core.CheckInsideResult.ERROR.getMsg()).build();
            CheckResult checkInfo = checkResultBuilder.details(checkDetails).build();
            return EngineCheckRes.success(checkInfo);
        }
    }

}

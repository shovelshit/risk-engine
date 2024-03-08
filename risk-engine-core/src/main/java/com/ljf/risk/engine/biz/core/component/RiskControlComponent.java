package com.ljf.risk.engine.biz.core.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
@RestController
public class RiskControlComponent {

    private final EventComponent eventComponent;
    private final com.ljf.risk.engine.biz.core.component.EventRuleRelationComponent eventRuleRelationComponent;
    private final com.ljf.risk.engine.biz.core.component.RuleComponent ruleComponent;
    private final InitComponent initComponent;

    public RiskControlComponent(EventComponent eventComponent, com.ljf.risk.engine.biz.core.component.EventRuleRelationComponent eventRuleRelationComponent, com.ljf.risk.engine.biz.core.component.RuleComponent ruleComponent, InitComponent initComponent) {
        this.eventComponent = eventComponent;
        this.eventRuleRelationComponent = eventRuleRelationComponent;
        this.ruleComponent = ruleComponent;
        this.initComponent = initComponent;
    }

//    @Override
//    @PostMapping("check")
//    public EngineApi.EngineCheckRes check(@RequestBody EngineCheckReq engineCheckReq) {
//        String eventCode = engineCheckReq.getEventCode();
//        EngineCheckRes checkRes = EngineCheckRes.success();
//        CheckDetails.CheckDetailsBuilder checkDetailsBuilder = CheckDetails.builder();
//        CheckResult.CheckResultBuilder checkResultBuilder = CheckResult.builder().eventCode(eventCode).code(CheckOutsideResult.PASS.getCode()).msg(CheckOutsideResult.PASS.getMsg());
//        checkRes.checkResult(checkResultBuilder.build());
//        Date eventTime = new Date();
//        try {
//            Event event = eventComponent.getCacheByCode(eventCode);
//            if (Objects.isNull(event)) {
//                log.warn("event is empty, checkReq: {}", engineCheckReq);
//                CheckDetails checkDetails = checkDetailsBuilder.failedCode(CheckInsideResult.NO_EVENT.getCode()).failedMsg(CheckInsideResult.NO_EVENT.getMsg()).build();
//                CheckResult checkResult = checkResultBuilder.details(checkDetails).build();
//                return checkRes.checkResult(checkResult);
//            }
//            checkResultBuilder.eventCodeDesc(event.getDescription());
//
//            if (!CommonStatus.success(event.getAnalysis()) && !CommonStatus.success(event.getAccumulate())) {
//                log.warn("event do not need to be analyzed and accumulate, {}", event);
//                CheckDetails checkDetails = checkDetailsBuilder.failedCode(CheckInsideResult.EVENT_OFFLINE.getCode()).failedMsg(CheckInsideResult.EVENT_OFFLINE.getMsg()).build();
//                CheckResult checkResult = checkResultBuilder.details(checkDetails).build();
//                return checkRes.checkResult(checkResult);
//            }
//
//            try (CurrentContext ignored = new CurrentContext(CurrentContext.Context.builder().eventTime(eventTime).event(event).engineCheckReq(engineCheckReq).build())) {
//                List<Rule> rules = eventRuleRelationComponent.getCacheByEventId(event.getId());
//                if (CollectionUtils.isEmpty(rules)) {
//                    log.warn("no rule configuration, checkReq: {}", engineCheckReq);
//                    CheckDetails checkDetails = checkDetailsBuilder.failedCode(CheckInsideResult.NO_RULE.getCode()).failedMsg(CheckInsideResult.NO_RULE.getMsg()).build();
//                    CheckResult checkResult = checkResultBuilder.details(checkDetails).build();
//                    eventComponent.accumulate();
//                    return checkRes.checkResult(checkResult);
//                }
//                if (CommonStatus.success(event.getAnalysis())) {
//                    checkRes = ruleComponent.analyse(rules);
//                }
//                eventComponent.accumulate();
//                return checkRes;
//            }
//        } catch (Exception e) {
//            log.error("engine check failed: ", e);
//            CheckDetails checkDetails = checkDetailsBuilder.failedCode(CheckInsideResult.ERROR.getCode()).failedMsg(CheckInsideResult.ERROR.getMsg()).build();
//            CheckResult checkInfo = checkResultBuilder.details(checkDetails).build();
//            return EngineCheckRes.success(checkInfo);
//        }
//    }
//
//
//    @Override
//    @PostMapping("checkCallback")
//    public EngineCheckRes checkCallback() {
//        return EngineCheckRes.success();
//    }
//
//    @Override
//    @GetMapping("load")
//    public EngineCheckRes load() {
//        return initComponent.load() ? EngineCheckRes.success() : EngineCheckRes.failed(Result.ERROR);
//    }
}

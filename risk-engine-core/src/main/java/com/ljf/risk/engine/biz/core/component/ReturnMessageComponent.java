package com.ljf.risk.engine.biz.core.component;

import com.ljf.risk.engine.biz.core.component.load.AbstractComponent;
import com.ljf.risk.engine.biz.service.ReturnMessageService;
import com.ljf.risk.engine.common.entity.ReturnMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@Component
public class ReturnMessageComponent extends AbstractComponent<Long, ReturnMessage> {

    private final ReturnMessageService returnMessageService;

    public ReturnMessageComponent(ReturnMessageService returnMessageService) {
        this.returnMessageService = returnMessageService;
    }

    @Override
    public String getComponentName() {
        return "拒绝文案";
    }

    @Override
    public void load() {
        List<ReturnMessage> returnMessages = returnMessageService.list();
        this.cache = returnMessages.stream().collect(Collectors.toConcurrentMap(ReturnMessage::getId, returnMessage -> returnMessage));
    }

    public ReturnMessage getCacheByReturnMessageId(Long returnMessageId) {
        if (returnMessageId == null) {
            return null;
        }
        return cache.get(returnMessageId);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

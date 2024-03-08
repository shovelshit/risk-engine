package com.ljf.risk.engine.biz.core.comparator.impl;

import org.apache.commons.lang3.StringUtils;
import com.ljf.risk.engine.biz.core.comparator.Comparator;
import org.springframework.stereotype.Component;

/**
 * @author lijinfeng
 * 等于
 **/
@Component
public class Eq implements Comparator {

    @Override
    public boolean compare(Object left, Object right) {
        if (left == null || right == null) {
            return false;
        }
        return StringUtils.equals(left.toString(), right.toString());
    }

}

package com.ljf.risk.engine.biz.core.indicator;


import com.ljf.risk.engine.common.entity.Indicator;
import java.util.List;

/**
 * @author lijinfeng
 */
public interface IndicatorHandle {

    void accumulate(List<Indicator> indicators);

    void analysis(List<Indicator> indicators);

}

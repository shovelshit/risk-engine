package com.ljf.risk.engine.biz.core.punish;

import com.ljf.risk.engine.biz.core.constants.CurrentContext;
import com.ljf.risk.engine.common.entity.Punish;

/**
 * @author lijinfeng
 */
public interface CustomPunish {

	void punish(CurrentContext.Context context, Punish punish);

}

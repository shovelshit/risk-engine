package com.ljf.risk.engine.web.function;

import java.util.Map;

public interface CustomFunction {
	
	/**
	 * 函数执行
	 * @param context
	 * @param params
	 * @return
	 */
	Object execute(Map context, String... params);

}

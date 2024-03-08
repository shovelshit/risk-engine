package com.ljf.risk.engine.common.validation.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author lijinfeng
 * @Description 实体校验结果
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResult {

	/**
	 * 是否有异常
	 */
	private boolean hasErrors;

	/**
	 * 异常消息记录
	 */
	private Map<String, String> errorMsg;

	/**
	 * 获取异常消息组装
	 *
	 * @return
	 */
	public String getMessage() {
		if (errorMsg == null || errorMsg.isEmpty()) {
			return "";
		}
		StringBuilder message = new StringBuilder();
		errorMsg.forEach((key, value) -> message.append(MessageFormat.format("{0}:{1} \r\n", key, value)));
		return message.toString();
	}
}

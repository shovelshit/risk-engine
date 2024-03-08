package com.ljf.risk.engine.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageReq<T> {

	private T data;

	@NotNull
	private Long size;

	@NotNull
	private Long current;
}

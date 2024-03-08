package com.ljf.risk.engine.common.validation.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * @author lijinfeng
 */
@Data
@ToString
public class ValidationBaseModel<T> implements Serializable {

    @Valid
    private T data;
}

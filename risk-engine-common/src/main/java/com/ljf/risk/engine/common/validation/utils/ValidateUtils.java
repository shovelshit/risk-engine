package com.ljf.risk.engine.common.validation.utils;


import com.ljf.risk.engine.common.utils.SpringBootBeanUtil;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author lijinfeng
 */
public class ValidateUtils {

    /**
     * 线程安全的，直接构建也可以，这里使用静态代码块一样的效果
     */
    private static final Validator validator = (Validator) SpringBootBeanUtil.getBean("validator");

    /**
     * 校验实体，返回实体所有属性的校验结果
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> ValidationResult fastValidateEntity(T obj) {
        // 解析校验结果
        return buildValidationResult(validator.validate(obj, Default.class));
    }

    /**
     * 校验实体，返回实体所有属性的校验结果
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> ValidationResult fastValidateProperty(T obj, String property, Class<?>... groups) {
        // 解析校验结果
        return buildValidationResult(validator.validateProperty(obj, property, groups));
    }


    public static <T> ValidationResult fastValidateEntity(T obj, Class<?>... groups) {
        // 解析校验结果
        return buildValidationResult(validator.validate(obj, groups));
    }

    /**
     * 将异常结果封装返回
     *
     * @param validateSet
     * @param <T>
     * @return
     */
    private static <T> ValidationResult buildValidationResult(Set<ConstraintViolation<T>> validateSet) {
        ValidationResult validationResult = new ValidationResult();
        if (!CollectionUtils.isEmpty(validateSet)) {
            validationResult.setHasErrors(true);
            Map<String, String> errorMsgMap = new HashMap<>();
            for (ConstraintViolation<T> constraintViolation : validateSet) {
                errorMsgMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
            validationResult.setErrorMsg(errorMsgMap);
        }
        return validationResult;
    }

}

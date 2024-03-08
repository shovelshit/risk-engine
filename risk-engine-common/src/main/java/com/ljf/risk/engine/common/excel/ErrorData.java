package com.ljf.risk.engine.common.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.data.annotation.Transient;

/**
 * @author lijinfeng
 */
@Data
public class ErrorData {

    @Transient
    @TableField(exist = false)
    @ExcelProperty("错误信息")
    private String errorMsg;

}

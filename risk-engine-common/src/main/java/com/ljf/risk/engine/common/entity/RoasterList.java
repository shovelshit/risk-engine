package com.ljf.risk.engine.common.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ljf.risk.engine.common.excel.ErrorData;
import com.ljf.risk.engine.common.validation.group.ImportGroup;
import com.ljf.risk.engine.common.validation.group.InsertGroup;
import com.ljf.risk.engine.common.validation.group.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 名单
 *
 * @author lijinfeng
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("t_engine_roaster_list")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoasterList extends ErrorData implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ExcelIgnore
    private Long id;

    @ExcelProperty("名单值")
    @NotNull(groups = {ImportGroup.class, InsertGroup.class, UpdateGroup.class})
    private String value;

    @ExcelIgnore
    @NotNull(groups = {InsertGroup.class, UpdateGroup.class})
    private Long roasterId;

    @ExcelProperty("备注")
    private String remark;

    @ExcelIgnore
    private Date createTime;

    @ExcelIgnore
    private Date updateTime;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class})
    @ExcelIgnore
    private Date expiredAt;

    @ExcelIgnore
    private String updateUser;
}

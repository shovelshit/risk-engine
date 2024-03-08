package com.ljf.risk.engine.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.ljf.risk.engine.common.entity.constants.Logic;
import com.ljf.risk.engine.common.validation.group.DeleteGroup;
import com.ljf.risk.engine.common.validation.group.InsertGroup;
import com.ljf.risk.engine.common.validation.group.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author lijinfeng
 */
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"id"})
@TableName("t_engine_function_extend")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FunctionExtend extends Model<FunctionExtend> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {DeleteGroup.class, UpdateGroup.class})
    private Long id;

    @NotBlank(groups = {InsertGroup.class})
    private String code;

    private String description;

    private Logic logic;

    private String functionCode;

    private String params;

    @Version
    private Long version;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;


}

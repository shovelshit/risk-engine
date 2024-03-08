package com.ljf.risk.engine.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.ljf.risk.engine.common.entity.constants.ConditionRelationType;
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
import java.util.List;

/**
 * @author lijinfeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_engine_condition_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConditionGroup extends Model<ConditionGroup> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {DeleteGroup.class, UpdateGroup.class})
    private Long id;

    @NotBlank(groups = {InsertGroup.class})
    private String code;

    private String description;

    private Logic logic;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    @NotNull
    private Long relationId;

    @NotNull
    private ConditionRelationType relationType;

    @TableField(exist = false)
    private List<Condition> conditionList;

}

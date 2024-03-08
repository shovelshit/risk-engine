package com.ljf.risk.engine.common.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonValue;


import com.ljf.risk.engine.common.entity.constants.Logic;
import com.ljf.risk.engine.common.entity.enums.RuleResult;
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
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_engine_rule")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rule extends Model<Rule> {
    private static final long serialVersionUID = 1L;
    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {DeleteGroup.class, UpdateGroup.class})
    private Long id;

    /**
     * Name
     */
    @NotBlank(groups = {InsertGroup.class})
    private String code;

    /**
     * Description
     */
    private String description;

    /**
     * Status
     */
    private Status status;

    private Logic logic;

    private Integer priority;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private Boolean test;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long returnMessageId;

    private RuleResult result;

    private Long ruleGroupId;

    @AllArgsConstructor
    @NoArgsConstructor
    public enum Status {
        ONLINE(1, "上线"),
        OFFLINE(0, "下线");

        @EnumValue
        private int code;

        @JsonValue
        private String description;

    }



}

package com.ljf.risk.engine.common.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ljf.risk.engine.common.entity.constants.Comparator;
import com.ljf.risk.engine.common.entity.constants.ConditionRelationType;
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
@EqualsAndHashCode(callSuper = false)
@TableName("t_engine_condition")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Condition extends Model<Condition> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {DeleteGroup.class, UpdateGroup.class})
    private Long id;

    @NotBlank(groups = {InsertGroup.class})
    private String code;

    private String description;

    private PropertyType leftType;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String leftProperty;

    private String leftValue;

    private PropertyType rightType;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String rightProperty;

    private String rightValue;

    private String createBy;

    private Comparator comparator;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    @NotNull
    private Long relationId;

    @NotNull
    private ConditionRelationType relationType;

    @NoArgsConstructor
    @AllArgsConstructor
    public enum PropertyType {
        //
        CONSTANT(0, "常量"),
        CONTEXT_PROPERTY(1, "上下文属性"),
        FUNCTION(2, "函数"),
        INDICATOR(3, "指标");

        @EnumValue
        private int code;

        @JsonValue
        private String description;
    }

    public String getConditionDetail(Object leftValue, Object rightValue) {
        String left = "";
        if (PropertyType.CONSTANT.equals(leftType)) {
            left += leftValue;
        } else {
            left += "(" + leftProperty + ")" + leftValue;
        }
        String c = comparator.getLogicOperator();
        String right = "";
        if (PropertyType.CONSTANT.equals(rightType)) {
            right += rightValue;
        } else {
            right += "(" + rightProperty + ")" + rightValue;
        }
        return left + c + right;
    }


}

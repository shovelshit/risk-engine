package com.ljf.risk.engine.common.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonValue;
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
@TableName("t_engine_rule_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleGroup extends Model<RuleGroup> {
	private static final long serialVersionUID = 1L;
	/**
	 * Id
	 */
	@TableId(value = "id",type = IdType.AUTO)
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

	private String createBy;

	private Date createTime;

	private String updateBy;

	private Date updateTime;

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

package com.ljf.risk.engine.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.ljf.risk.engine.common.entity.constants.CommonStatus;
import com.ljf.risk.engine.common.entity.constants.Status;
import com.ljf.risk.engine.common.validation.group.DeleteGroup;
import com.ljf.risk.engine.common.validation.group.InsertGroup;
import com.ljf.risk.engine.common.validation.group.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * @author lijinfeng
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_engine_event")
public class Event extends Model<Event> {
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
	 * Analysis
	 */
	private CommonStatus analysis;

	/**
	 * Accumulate
	 */
	private CommonStatus accumulate;


	/**
	 * Status
	 */
	private Status status;

	private String createBy;

	private Date createTime;

	private String updateBy;

	private Date updateTime;

	@Version
	private Integer version;

}

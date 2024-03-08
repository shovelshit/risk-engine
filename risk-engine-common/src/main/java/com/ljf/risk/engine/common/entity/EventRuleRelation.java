package com.ljf.risk.engine.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.ljf.risk.engine.common.validation.group.DeleteGroup;
import com.ljf.risk.engine.common.validation.group.InsertGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author lijinfeng
 */
@EqualsAndHashCode(callSuper = true, exclude = {"id"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_engine_event_rule_relation")
public class EventRuleRelation extends Model<EventRuleRelation> {
	private static final long serialVersionUID = 1L;
	/**
	 * Id
	 */
	@TableId(value = "id",type = IdType.AUTO)
	private Long id;

	@NotNull(groups = {InsertGroup.class, DeleteGroup.class})
	private Long eventId;

	@NotNull(groups = {InsertGroup.class, DeleteGroup.class})
	private Long ruleId;

}

package com.ljf.risk.engine.biz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljf.risk.engine.common.entity.ConditionGroup;
import com.ljf.risk.engine.common.entity.constants.ConditionRelationType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lijinfeng
 */
public interface ConditionGroupDao extends BaseMapper<ConditionGroup> {
    List<ConditionGroup> listConditionGroup(@Param("relationId") Long relationId, @Param("conditionRelationType") ConditionRelationType conditionRelationType);
}

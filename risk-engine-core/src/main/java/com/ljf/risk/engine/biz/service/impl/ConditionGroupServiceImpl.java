package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.service.ConditionGroupService;
import com.ljf.risk.engine.common.entity.ConditionGroup;
import com.ljf.risk.engine.common.entity.constants.ConditionRelationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.ljf.risk.engine.biz.dao.ConditionGroupDao;

import java.util.List;

/**
 * @author lijinfeng
 */
@Service
@Slf4j
public class ConditionGroupServiceImpl extends ServiceImpl<ConditionGroupDao, ConditionGroup> implements ConditionGroupService {
    @Override
    public List<ConditionGroup> listConditionGroup(Long relationId, ConditionRelationType conditionRelationType) {
        return baseMapper.listConditionGroup(relationId, conditionRelationType );
    }
}

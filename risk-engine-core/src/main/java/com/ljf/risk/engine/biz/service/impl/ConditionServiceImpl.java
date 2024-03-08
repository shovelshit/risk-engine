package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.dao.ConditionDao;
import com.ljf.risk.engine.biz.dao.RuleDao;
import com.ljf.risk.engine.biz.service.ConditionGroupService;
import com.ljf.risk.engine.biz.service.ConditionService;
import com.ljf.risk.engine.common.entity.Condition;
import com.ljf.risk.engine.common.entity.ConditionGroup;
import com.ljf.risk.engine.common.entity.constants.ConditionRelationType;
import com.ljf.risk.engine.common.entity.request.createConditionsReq;
import com.ljf.risk.engine.common.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Service
@Slf4j
public class ConditionServiceImpl extends ServiceImpl<ConditionDao, Condition> implements ConditionService {
    private final ConditionGroupService conditionGroupService;

    public ConditionServiceImpl(ConditionGroupService conditionGroupService, RuleDao ruleDao) {
        this.conditionGroupService = conditionGroupService;
    }

    @Override
    public List<Condition> listCondition(Long relationId, ConditionRelationType conditionRelationType) {
        LambdaQueryWrapper<Condition> qw = new LambdaQueryWrapper<>();
        if (Objects.isNull(relationId) && Objects.isNull(conditionRelationType)) {
            return new ArrayList<>();
        }
        qw.eq(Condition::getRelationId, relationId).eq(Condition::getRelationType, conditionRelationType);
        log.info("listCondition query: {}", qw);
        return list(qw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createConditions(createConditionsReq req) {
        return handleCondition(req) && handleConditionGroup(req);
    }

    private Boolean handleConditionGroup(createConditionsReq req) {
        String username = ThreadLocalUtils.getUsername();
        List<ConditionGroup> oldConditionGroups = conditionGroupService.listConditionGroup(req.getRelationId(), req.getRelationType());
        List<ConditionGroup> modifyConditionGroups = req.getConditionGroups();
        List<ConditionGroup> toUpdate = modifyConditionGroups.stream().filter(d -> Objects.nonNull(d.getId())).collect(Collectors.toList());
        List<Long> updateGroupIds = toUpdate.stream().map(ConditionGroup::getId).collect(Collectors.toList());
        List<Long> oldConditionGroupIds = oldConditionGroups.stream().map(ConditionGroup::getId).collect(Collectors.toList());

        // 新增
        List<ConditionGroup> toAdd = modifyConditionGroups.stream().filter(d -> Objects.isNull(d.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(toAdd)) {
            for (ConditionGroup conditionGroup : toAdd) {
                conditionGroup.setCreateBy(username);
                conditionGroup.setUpdateBy(username);
                conditionGroup.setRelationType(req.getRelationType());
            }
            if (!conditionGroupService.saveBatch(toAdd)) {
                log.info("[handleConditionGroup] insert conditionGroup failed: {}", toAdd);
                throw new RuntimeException("新增规则组失败");
            }
            ArrayList<Condition> toAddConditions = new ArrayList<>();
            for (ConditionGroup conditionGroup : toAdd) {
                for (Condition condition : conditionGroup.getConditionList()) {
                    condition.setCreateBy(username);
                    condition.setUpdateBy(username);
                    condition.setRelationId(conditionGroup.getId());
                    condition.setRelationType(ConditionRelationType.CONDITION_GROUP);
                    toAddConditions.add(condition);
                }
            }
            if (CollectionUtils.isNotEmpty(toAddConditions)) {
                if (!saveBatch(toAddConditions)) {
                    log.info("[handleConditionGroup] insert conditionGroup relation condition failed: {}", toAddConditions);
                    throw new RuntimeException("新增规则组规则失败");
                }
            }
        }

        // 更新
        toUpdate.removeAll(oldConditionGroups);
        if (CollectionUtils.isNotEmpty(toUpdate)) {
            ArrayList<Condition> toUpdateCondition = new ArrayList<>();
            ArrayList<Condition> toAddCondition = new ArrayList<>();
            ArrayList<Long> toRemoveCondition = new ArrayList<>();
            for (ConditionGroup conditionGroup : toUpdate) {
                List<Condition> conditionList = conditionGroup.getConditionList();
                conditionGroup.setUpdateBy(username);
                ArrayList<Long> toUpdateIds = new ArrayList<>();
                for (Condition condition : conditionList) {
                    if (Objects.isNull(condition.getId())) {
                        condition.setCreateBy(username);
                        condition.setUpdateBy(username);
                        condition.setRelationType(ConditionRelationType.CONDITION_GROUP);
                        toAddCondition.add(condition);
                    } else {
                        condition.setUpdateBy(username);
                        toUpdateIds.add(condition.getId());
                        toUpdateCondition.add(condition);
                    }
                }
                Optional<ConditionGroup> oldConditionGroup = oldConditionGroups.stream().filter(d -> Objects.equals(d.getId(), conditionGroup.getId())).findFirst();
                oldConditionGroup.ifPresent(d -> {
                    List<Condition> oldConditions = d.getConditionList();
                    List<Long> oldIds = oldConditions.stream().map(Condition::getId).filter(id -> !toUpdateIds.contains(id)).collect(Collectors.toList());
                    toRemoveCondition.addAll(oldIds);
                });

            }
            if (!conditionGroupService.updateBatchById(toUpdate)) {
                log.info("[handleConditionGroup] update conditionGroup failed: {}", toUpdate);
                throw new RuntimeException("更新规则组失败");
            }
            if (CollectionUtils.isNotEmpty(toUpdateCondition)) {
                if (!updateBatchById(toUpdateCondition)) {
                    log.info("[handleConditionGroup] update conditionGroup condition failed: {}", toUpdateCondition);
                    throw new RuntimeException("更新规则组规则失败");
                }
            }
            if (CollectionUtils.isNotEmpty(toAddCondition)) {
                if (!saveBatch(toAddCondition)) {
                    log.info("[handleConditionGroup] add conditionGroup condition failed: {}", toUpdateCondition);
                    throw new RuntimeException("新增规则组规则失败");
                }
            }
            if (CollectionUtils.isNotEmpty(toRemoveCondition)) {
                if (!removeBatchByIds(toRemoveCondition)) {
                    log.info("[handleConditionGroup] remove conditionGroup condition failed: {}", toUpdateCondition);
                    throw new RuntimeException("删除规则组规则失败");
                }
            }
        }


        // 删除
        oldConditionGroupIds.removeAll(updateGroupIds);
        if (CollectionUtils.isNotEmpty(oldConditionGroupIds)) {
            if (!conditionGroupService.removeBatchByIds(oldConditionGroupIds)) {
                log.info("[handleConditionGroup] remover condition failed: {}", oldConditionGroupIds);
                throw new RuntimeException("删除规则组失败");
            }
            List<Long> ids = oldConditionGroups.stream()
                    .filter(d -> oldConditionGroupIds.contains(d.getId()))
                    .flatMap(d -> d.getConditionList().stream())
                    .map(Condition::getId)
                    .collect(Collectors.toList());
            if (!removeBatchByIds(ids)) {
                log.info("[handleConditionGroup] remove conditionGroup relation condition failed: {}", ids);
                throw new RuntimeException("删除规则组规则失败");
            }
        }
        return true;
    }

    private Boolean handleCondition(createConditionsReq req) {
        String username = ThreadLocalUtils.getUsername();
        List<Condition> oldConditions = this.listCondition(req.getRelationId(), req.getRelationType());
        List<Condition> modifyConditions = req.getConditions();
        List<Condition> toUpdate = modifyConditions.stream().filter(d -> Objects.nonNull(d.getId())).collect(Collectors.toList());
        List<Long> updateIds = toUpdate.stream().map(Condition::getId).collect(Collectors.toList());
        List<Long> oldConditionIds = oldConditions.stream().map(Condition::getId).collect(Collectors.toList());

        // 新增
        List<Condition> toAdd = modifyConditions.stream().filter(d -> Objects.isNull(d.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(toAdd)) {
            for (Condition condition : toAdd) {
                condition.setCreateBy(username);
                condition.setUpdateBy(username);
                condition.setRelationType(req.getRelationType());
            }
            if (!saveBatch(toAdd)) {
                log.info("[handleCondition] insert condition failed: {}", toAdd);
                throw new RuntimeException("新增规则失败");
            }
        }

        // 更新
        toUpdate.removeAll(oldConditions);
        if (CollectionUtils.isNotEmpty(toUpdate)) {
            for (Condition condition : toUpdate) {
                condition.setUpdateBy(username);
            }
            if (!updateBatchById(toUpdate)) {
                log.info("[handleCondition] update condition failed: {}", toUpdate);
                throw new RuntimeException("更新规则失败");
            }
        }

        // 删除
        oldConditionIds.removeAll(updateIds);
        if (CollectionUtils.isNotEmpty(oldConditionIds)) {
            if (!removeBatchByIds(oldConditionIds)) {
                log.info("[handleCondition] remover condition failed: {}", oldConditionIds);
                throw new RuntimeException("删除规则失败");
            }
        }
        return true;
    }

}

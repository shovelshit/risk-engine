package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.dao.RoasterDao;
import com.ljf.risk.engine.biz.service.RoasterService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.Roaster;
import com.ljf.risk.engine.common.utils.ThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class RoasterServiceImpl extends ServiceImpl<RoasterDao, Roaster> implements RoasterService {
    @Override
    public IPage<Roaster> pageData(PageReq<Roaster> roasterModelPageReq) {
        LambdaQueryWrapper<Roaster> qw = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(roasterModelPageReq.getData().getCode())) {
            qw.eq(Roaster::getCode, roasterModelPageReq.getData().getCode());
        }
        if (StringUtils.isNotBlank(roasterModelPageReq.getData().getName())) {
            qw.like(Roaster::getName, roasterModelPageReq.getData().getName());
        }
        qw.orderByDesc(Roaster::getUpdateTime);
        return page(new Page<>(roasterModelPageReq.getCurrent(), roasterModelPageReq.getSize()), qw);
    }

    @Override
    public boolean update(Roaster roaster) {
        roaster.setUpdateUser(ThreadLocalUtils.getUsername());
        return updateById(roaster);
    }

    @Override
    public boolean delete(Roaster roaster) {
//        long count = eventRuleRelationService.count(new LambdaQueryWrapper<EventRuleRelation>().eq(EventRuleRelation::getEventId, event.getId()));
//        if (count > 0) {
//            throw new RuntimeException("删除失败:该事件下已绑定规则组");
//        }
        return removeById(roaster);
    }

    @Override
    public boolean add(Roaster roaster) {
        boolean exists = baseMapper.exists(new LambdaQueryWrapper<Roaster>().eq(Roaster::getCode, roaster.getCode()));
        if (exists) {
            throw new RuntimeException("该名单集已存在");
        }
        String username = ThreadLocalUtils.getUsername();
        roaster.setUpdateUser(username);
        roaster.setCreateTime(new Date());
        return save(roaster);
    }
}

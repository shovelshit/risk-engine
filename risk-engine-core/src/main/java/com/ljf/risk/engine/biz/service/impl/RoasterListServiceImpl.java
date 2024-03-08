package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.dao.RoasterListDao;
import com.ljf.risk.engine.biz.service.RoasterListService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.RoasterList;
import com.ljf.risk.engine.common.utils.ThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Objects;

@Repository
public class RoasterListServiceImpl extends ServiceImpl<RoasterListDao, RoasterList> implements RoasterListService {
    @Override
    public IPage<RoasterList> pageData(PageReq<RoasterList> roasterListModelPageReq) {
        LambdaQueryWrapper<RoasterList> qw = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(roasterListModelPageReq.getData().getValue())) {
            qw.eq(RoasterList::getValue, roasterListModelPageReq.getData().getValue());
        }
        if (!Objects.isNull(roasterListModelPageReq.getData().getRoasterId())) {
            qw.eq(RoasterList::getRoasterId, roasterListModelPageReq.getData().getRoasterId());
        }
        qw.orderByDesc(RoasterList::getUpdateTime);
        return page(new Page<>(roasterListModelPageReq.getCurrent(), roasterListModelPageReq.getSize()), qw);
    }

    @Override
    public boolean update(RoasterList roasterList) {
        roasterList.setUpdateUser(ThreadLocalUtils.getUsername());
        return updateById(roasterList);
    }

    @Override
    public boolean delete(RoasterList roasterList) {
        return removeById(roasterList);
    }

    @Override
    public boolean add(RoasterList roasterList) {
        boolean exists = baseMapper.exists(new LambdaQueryWrapper<RoasterList>().eq(RoasterList::getValue, roasterList.getValue()));
        if (exists) {
            throw new RuntimeException("该名单已存在");
        }
        String username = ThreadLocalUtils.getUsername();
        roasterList.setUpdateUser(username);
        roasterList.setCreateTime(new Date());
        return save(roasterList);
    }
}

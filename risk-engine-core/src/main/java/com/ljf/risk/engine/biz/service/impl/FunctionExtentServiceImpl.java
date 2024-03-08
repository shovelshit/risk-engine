package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.dao.FunctionExtentDao;
import com.ljf.risk.engine.biz.service.FunctionExtentService;
import com.ljf.risk.engine.common.entity.FunctionExtend;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author lijinfeng
 */
@Service
@Slf4j
public class FunctionExtentServiceImpl extends ServiceImpl<FunctionExtentDao, FunctionExtend> implements FunctionExtentService {
    @Override
    public IPage<FunctionExtend> pageFunction(PageReq<FunctionExtend> pageReq) {
        LambdaQueryWrapper<FunctionExtend> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(FunctionExtend::getId);
        if (StringUtils.isNotBlank(pageReq.getData().getCode())) {
            qw.eq(FunctionExtend::getCode, pageReq.getData().getCode());
        }
        return page(new Page<>(pageReq.getCurrent(), pageReq.getSize()), qw);
    }

    @Override
    public boolean update(FunctionExtend functionExtend) {
        functionExtend.setUpdateBy(ThreadLocalUtils.getUsername());
        return updateById(functionExtend);
    }

    @Override
    public boolean delete(FunctionExtend functionExtend) {
        return removeById(functionExtend);
    }

    @Override
    public boolean add(FunctionExtend functionExtend) {
        boolean exists = baseMapper.exists(new LambdaQueryWrapper<FunctionExtend>().eq(FunctionExtend::getCode, functionExtend.getCode()));
        if (exists) {
            throw new RuntimeException("该函数已存在");
        }
        String username = ThreadLocalUtils.getUsername();
        functionExtend.setCreateBy(username);
        functionExtend.setUpdateBy(username);
        return save(functionExtend);
    }
}

package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.dao.ReturnMessageDao;
import com.ljf.risk.engine.biz.service.ReturnMessageService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.ReturnMessage;
import com.ljf.risk.engine.common.utils.ThreadLocalUtils;
import org.springframework.stereotype.Service;

/**
 * @author lijinfeng
 */
@Service
public class ReturnMessageServiceImpl extends ServiceImpl<ReturnMessageDao, ReturnMessage> implements ReturnMessageService {
    @Override
    public IPage<ReturnMessage> pageReturnCode(PageReq<ReturnMessage> rulePageReq) {
        QueryWrapper<ReturnMessage> qw = new QueryWrapper<>();
        qw.orderByDesc("update_time");
        return page(new Page<>(rulePageReq.getCurrent(), rulePageReq.getSize()), qw);
    }

    @Override
    public boolean update(ReturnMessage returnMessage) {
        returnMessage.setUpdateBy(ThreadLocalUtils.getUsername());
        return updateById(returnMessage);
    }

    @Override
    public boolean delete(ReturnMessage returnMessage) {
        return removeById(returnMessage);
    }

    @Override
    public boolean add(ReturnMessage returnMessage) {
        boolean exists = baseMapper.exists(new LambdaQueryWrapper<ReturnMessage>().eq(ReturnMessage::getReturnMessage, returnMessage.getReturnMessage()));
        if (exists) {
            throw new RuntimeException("该文案已存在");
        }
        String username = ThreadLocalUtils.getUsername();
        returnMessage.setCreateBy(username);
        returnMessage.setUpdateBy(username);
        return save(returnMessage);
    }
}



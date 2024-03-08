package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.dao.PunishDao;
import com.ljf.risk.engine.biz.service.PunishService;
import com.ljf.risk.engine.common.entity.Punish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author lijinfeng
 */
@Service
@Slf4j
public class PunishServiceImpl extends ServiceImpl<PunishDao, Punish> implements PunishService {
    @Override
    public boolean delete(Punish punish) {
        return removeById(punish);
    }

    @Override
    public boolean add(Punish punish) {
        Punish exists = getOne(new LambdaQueryWrapper<Punish>().eq(Punish::getRuleId, punish.getRuleId()).eq(Punish::getPunishCode, punish.getPunishCode()).eq(Punish::getPunishField, Arrays.toString(punish.getPunishField())).eq(Punish::getPunishPeriod, punish.getPunishPeriod()));
        if (exists != null) {
            throw new RuntimeException("该惩罚策略已存在");
        }
        return save(punish);
    }
}

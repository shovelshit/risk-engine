package com.ljf.risk.engine.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ljf.risk.engine.common.entity.Punish;

/**
 * @author lijinfeng
 */
public interface PunishService extends IService<Punish> {
    boolean delete(Punish punish);

    boolean add(Punish punish);
}

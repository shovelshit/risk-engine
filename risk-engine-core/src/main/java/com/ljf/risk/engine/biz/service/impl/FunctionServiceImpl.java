package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.dao.FunctionDao;
import com.ljf.risk.engine.biz.service.FunctionService;
import com.ljf.risk.engine.common.entity.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lijinfeng
 */
@Service
@Slf4j
public class FunctionServiceImpl extends ServiceImpl<FunctionDao, Function> implements FunctionService {
}

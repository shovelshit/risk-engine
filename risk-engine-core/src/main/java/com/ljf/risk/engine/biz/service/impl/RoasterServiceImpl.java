package com.ljf.risk.engine.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljf.risk.engine.biz.core.roster.RoasterHolder;
import com.ljf.risk.engine.biz.core.roster.holder.RedisRoasterHolder;
import com.ljf.risk.engine.biz.dao.RoasterDao;
import com.ljf.risk.engine.biz.service.RoasterListService;
import com.ljf.risk.engine.biz.service.RoasterService;
import com.ljf.risk.engine.common.entity.PageReq;
import com.ljf.risk.engine.common.entity.Roaster;
import com.ljf.risk.engine.common.entity.RoasterList;
import com.ljf.risk.engine.common.utils.SpringBootBeanUtil;
import com.ljf.risk.engine.common.utils.ThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoasterServiceImpl extends ServiceImpl<RoasterDao, Roaster> implements RoasterService {

    private final RoasterListService roasterListService;

    public RoasterServiceImpl(RoasterListService roasterListService) {
        this.roasterListService = roasterListService;
    }

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

    @Override
    public boolean addRoasterList(@PathVariable Long id, @RequestBody List<RoasterList> roasterListModel) {
        Roaster roaster = getById(id);
        if (roaster == null) {
            throw new RuntimeException("名单不存在");
        }
        List<RoasterList> roasters = new ArrayList<>(roasterListModel.size());
        roasterListModel.forEach(model -> {
            RoasterList roasterList = new RoasterList();
            BeanUtils.copyProperties(model, roasterList);
            roasterList.setRoasterId(id);
            roasters.add(roasterList);
        });

        try {
            if (roasterListService.saveBatch(roasters)) {
                //添加名单 刷新缓存
                SpringBootBeanUtil.getBean(RedisRoasterHolder.class).remove(roaster.getCode(), roasters.stream().map(RoasterList::getValue).collect(Collectors.toList()));
            }
        } catch (Exception ex) {
            log.error("addRoasterList failed: ", ex);
            if (ex instanceof DuplicateKeyException) {
                throw new RuntimeException("重复数据");
            }
        }
        return true;
    }

    @Override
    public boolean deleteLists(@PathVariable Long id, @RequestBody List<Long> roasterListModel) {
        Roaster roaster = getById(id);
        if (roaster == null) {
            throw new RuntimeException("名单不存在");
        }
        List<RoasterList> roasterLists = roasterListService.listByIds(roasterListModel);
        if (CollectionUtils.isEmpty(roasterLists)) {
            return true;
        }
        roasterListService.removeBatchByIds(roasterListModel);
        SpringBootBeanUtil.getBean(RedisRoasterHolder.class).remove(roaster.getCode(), roasterLists.stream().map(RoasterList::getValue).collect(Collectors.toList()));
        return true;
    }

    @Override
    public boolean createOrUpdate(@RequestBody Roaster roasterModel) {
        //code不能更新！！！！
        Roaster roaster = new Roaster();
        BeanUtils.copyProperties(roasterModel, roaster);
        roaster.setCreateTime(new Date());
        return saveOrUpdate(roaster);
    }

    @Override
    public boolean delete(@RequestBody List<Long> roasters) {
        //名单删除实际上需要校验名单在规则中的使用情况！！！
        List<Roaster> r = listByIds(roasters);
        if (CollectionUtils.isEmpty(r)) {
            return true;
        }
        List<RoasterList> roasterLists = roasterListService.list(new LambdaQueryWrapper<>(RoasterList.class).in(RoasterList::getRoasterId, roasters));
        Map<Long, List<Roaster>> mp = r.stream().collect(Collectors.groupingBy(Roaster::getId));
        if (!CollectionUtils.isEmpty(roasterLists)) {
            roasterListService.removeByIds(roasterLists.stream().map(RoasterList::getId).collect(Collectors.toList()));
        }
        removeBatchByIds(roasters);
        roasterLists
                .stream()
                .collect(Collectors.groupingBy(RoasterList::getRoasterId))
                .forEach((k, v) -> SpringBootBeanUtil.getBean(RedisRoasterHolder.class).remove(mp.get(k).get(0).getCode(), roasterLists
                        .stream()
                        .map(RoasterList::getValue)
                        .collect(Collectors.toList())));
        return true;
    }

    @Override
    public boolean update(@RequestBody RoasterList roasterListModel, @PathVariable Long roasterId) {
        Roaster roaster = getById(roasterId);
        if (roaster == null) {
            throw new RuntimeException("名单不存在");
        }
        RoasterList roasterList = roasterListService.getById(roasterListModel.getId());
        if (roasterList == null) {
            throw new RuntimeException("名单不存在");
        }
        roasterList.setUpdateTime(new Date());
        roasterList.setExpiredAt(roasterListModel.getExpiredAt());
        roasterList.setValue(roasterListModel.getValue());
        roasterList.setRemark(roasterListModel.getRemark());
        roasterList.setUpdateUser(roasterListModel.getUpdateUser());
        if (roasterListService.saveOrUpdate(roasterList)) {
            SpringBootBeanUtil.getBean(RedisRoasterHolder.class).remove(roaster.getCode(), roasterList.getValue());
            return true;
        }
        return true;
    }
}

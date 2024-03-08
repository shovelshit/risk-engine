package com.ljf.risk.engine.biz.core.roster.holder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljf.risk.engine.biz.core.component.load.AbstractComponent;
import com.ljf.risk.engine.biz.core.roster.RoasterHolder;
import com.ljf.risk.engine.biz.service.RoasterListService;
import com.ljf.risk.engine.biz.service.RoasterService;
import com.ljf.risk.engine.common.entity.Roaster;
import com.ljf.risk.engine.common.entity.RoasterList;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

public abstract class AbstractRoasterHolder<T, K, V> extends AbstractComponent<K, V> implements RoasterHolder {
    protected RoasterService roasterService;

    protected RoasterListService roasterListService;

    public AbstractRoasterHolder(RoasterService roasterService, RoasterListService roasterListService) {
        this.roasterService = roasterService;
        this.roasterListService = roasterListService;
    }

    @Transactional
    public synchronized void load() {
        List<Roaster> roasters = roasterService.list();
        if (!CollectionUtils.isEmpty(roasters)) {
            Date now = new Date();
            roasters.stream()
                    .forEach(roaster -> {
                        try {
                            List<RoasterList> roasterLists = roasterListService.list(new LambdaQueryWrapper<RoasterList>().eq(RoasterList::getRoasterId, roaster.getId()));
                            if (!CollectionUtils.isEmpty(roasterLists)) {
                                T target = prepare(roaster);
                                roasterLists.stream()
                                        .filter(r -> r.getExpiredAt() != null && r.getExpiredAt().compareTo(now) > 0).forEach(r -> onList(roaster, r, target));
                                onComplete(roaster, target);
                            }
                        } catch (Exception ex) {

                        }
                    });
        }
    }

    protected abstract T prepare(Roaster roaster);

    protected abstract void onList(Roaster roaster, RoasterList roasterList, T t);

    protected abstract void onComplete(Roaster roaster, T t);

    @Override
    public void refresh() {
        this.load();
    }
}

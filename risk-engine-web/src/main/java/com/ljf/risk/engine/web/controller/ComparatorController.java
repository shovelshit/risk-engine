package com.ljf.risk.engine.web.controller;

import com.google.common.collect.Lists;
import com.ljf.risk.engine.common.entity.constants.Comparator;
import com.ljf.risk.engine.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author lijinfeng
 */
@RestController
@RequestMapping("/engine/comparator")
@Slf4j
public class ComparatorController {

    @GetMapping("list")
    public Result listCondition() {
        Comparator[] values = Comparator.values();
        ArrayList<Object> data = Lists.newArrayList();
        for (Comparator value : values) {
            HashMap<String, String> temp = new HashMap<>();
            temp.put("value", value.name());
            temp.put("label", value.getDescription());
            data.add(temp);
        }
        return Result.succ().data(data);
    }

}

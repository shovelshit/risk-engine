package com.ljf.risk.engine.common.entity.response;

import com.ljf.risk.engine.common.entity.Event;
import com.ljf.risk.engine.common.entity.EventIndicatorRelation;
import com.ljf.risk.engine.common.entity.EventRuleRelation;
import com.ljf.risk.engine.common.entity.Punish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelationDataRes {
    private List<Event> events;
    private List<Punish> punishs;
    private List<EventRuleRelation> eventRuleRelations;
    private List<EventIndicatorRelation> eventIndicatorRelations;
    private Map<String, String> punishTypes;
}
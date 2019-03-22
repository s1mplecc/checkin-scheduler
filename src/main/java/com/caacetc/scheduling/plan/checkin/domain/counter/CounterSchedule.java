package com.caacetc.scheduling.plan.checkin.domain.counter;

import com.caacetc.scheduling.plan.checkin.domain.Interval;
import com.caacetc.scheduling.plan.checkin.mapper.CounterMapper;

import java.util.List;

public class CounterSchedule {
    private List<Counter> counters;

    public CounterSchedule() {
        counters = new CounterMapper().counters();
    }

    public CounterSchedule(List<Counter> counters) {
        this.counters = counters;
    }

    // todo 计算每个柜台需要开放的时间段
    public void schedule(List<Interval> intervals) {

    }
}

package com.caacetc.scheduling.plan.checkin.domain.counter;

import com.caacetc.scheduling.plan.checkin.domain.Interval;
import com.caacetc.scheduling.plan.checkin.mapper.CounterMapper;

import java.util.List;

/**
 * 柜台调度类，包括：
 * 1、必开柜台（开放时间段、柜台号），配置传入
 * 2、按需开放（计算所需柜台数大于必须开放数）
 */
public class CounterScheduler {
    private List<Counter> counters;
    private int maxIE;
    private int maxDE;
    private int maxP;


    public CounterScheduler() {
        counters = new CounterMapper().counters();
    }

    public CounterScheduler(List<Counter> counters) {
        this.counters = counters;
    }

    // todo 计算每个柜台需要开放的时间段
    public void schedule(List<Interval> intervals) {
//        intervals.stream()
//                .filter(interval -> interval.premiumCounters() > 11)
//                .forEach(System.out::println);
//
//        intervals.stream()
//                .filter(interval -> interval.dEconomyCounters() > 7)
//                .forEach(System.out::println);

        intervals.stream()
                .filter(interval -> interval.iEconomyCounters() > 11)
                .forEach(System.out::println);
    }
}

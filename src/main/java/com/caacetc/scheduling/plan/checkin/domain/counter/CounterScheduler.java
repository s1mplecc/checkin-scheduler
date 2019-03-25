package com.caacetc.scheduling.plan.checkin.domain.counter;

import com.caacetc.scheduling.plan.checkin.domain.Interval;
import com.caacetc.scheduling.plan.checkin.mapper.CounterMapper;

import java.util.Calendar;
import java.util.List;

/**
 * 柜台调度类，包括：
 * 1、必开柜台（开放时间段、柜台号），配置传入
 * 2、按需开放（计算所需柜台数大于必须开放数）
 */
public class CounterScheduler {
    private List<Counter> counters;
    private List<Counter> premCounters;
    private int maxIE;
    private int maxDE;
    private int maxP;

    public CounterScheduler() {
        counters = new CounterMapper().counters();
        premCounters = new CounterMapper().premCounters();
    }

    public CounterScheduler(List<Counter> counters) {
        this.counters = counters;
    }

    // todo 计算每个柜台需要开放的时间段
    public List<Counter> schedule(List<Interval> intervals) {
        intervals.stream()
                .forEach(interval -> {
                    int temp = Math.min(premCounters.size(), interval.premiumCounters());
                    for (int i = 0; i < temp; i++) {
                        Calendar endTime = (Calendar) interval.startTime().clone();
                        endTime.add(Calendar.MINUTE, 5);
                        premCounters.get(i).open(interval.startTime(), endTime);
                    }
                });

        intervals.stream()
                .filter(interval -> interval.iEconomyCounters() > 11)
                .forEach(System.out::println);

        return null;
    }
}

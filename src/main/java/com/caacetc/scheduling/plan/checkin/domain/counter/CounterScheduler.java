package com.caacetc.scheduling.plan.checkin.domain.counter;

import com.caacetc.scheduling.plan.checkin.domain.Interval;
import com.caacetc.scheduling.plan.checkin.mapper.CounterMapper;

import java.util.ArrayList;
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
    private List<Counter> dEconCounters;
    private List<Counter> iEconCounters;
    private int maxIE;
    private int maxDE;
    private int maxP;

    public CounterScheduler() {
        counters = new ArrayList<>();
        premCounters = new CounterMapper().premCounters();
        dEconCounters = new CounterMapper().dEconCounters();
        iEconCounters = new CounterMapper().iEconCounters();
    }

    public CounterScheduler(List<Counter> counters) {
        this.counters = counters;
    }

    /**
     * Compute each counter open periods
     */
    public List<Counter> schedule(List<Interval> intervals) {
        intervals.forEach(interval -> {
            schedule(interval, premCounters, interval.premiumCounters());
            schedule(interval, dEconCounters, interval.dEconomyCounters());
            schedule(interval, iEconCounters, interval.iEconomyCounters());
        });

        counters.addAll(premCounters);
        counters.addAll(dEconCounters);
        counters.addAll(iEconCounters);
        return counters;
    }

    private void schedule(Interval interval, List<Counter> counters, int needs) {
        int temp = Math.min(counters.size(), needs);
        for (int i = 0; i < temp; i++) {
            Calendar endTime = (Calendar) interval.startTime().clone();
            endTime.add(Calendar.MINUTE, 5);
            counters.get(i).open(interval.startTime(), endTime);
        }
    }
}

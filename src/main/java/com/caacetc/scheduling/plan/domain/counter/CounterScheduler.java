package com.caacetc.scheduling.plan.domain.counter;

import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.domain.flight.PassengerDistribution;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 柜台调度类，包括：
 * 1、必开柜台（开放时间段、柜台号），配置传入
 * 2、按需开放（计算所需柜台数大于必须开放数）
 */
@Service
public class CounterScheduler {
    @Resource
    private CounterRepository repository;

    public CounterScheduler() {
        repository = new CounterRepository();
    }

    /**
     * Compute each counter open periods
     */
    public List<Counter> scheduleBy(List<PassengerDistribution> passengerDistributions, List<Flight> flights) {
        List<Counter> result = new ArrayList<>();

        List<Counter> mustOpenCounters = repository.mustOpenCounters();
        List<Counter> onDemandPremiumCounters = repository.onDemandPremiumCounters();
        List<Counter> onDemandIntEconomyCounters = repository.onDemandIntEconomyCounters();
        List<Counter> onDemandDomEconomyCounters = repository.onDemandDomEconomyCounters();

        scheduleMustOpenCounters(mustOpenCounters);
        passengerDistributions.forEach(distribution -> {
            scheduleBy(distribution, onDemandPremiumCounters, distribution.premiumCounters());
            scheduleBy(distribution, onDemandDomEconomyCounters, distribution.domEconomyCounters());
            scheduleBy(distribution, onDemandIntEconomyCounters, distribution.intEconomyCounters() - 11); // 国际经济舱人工办理柜台必须开放个数
        });

        result.addAll(mustOpenCounters);
        result.addAll(onDemandDomEconomyCounters);
        result.addAll(onDemandIntEconomyCounters);
        result.addAll(onDemandPremiumCounters);

        return result.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * @param distribution passenger distribution divided by 5 minutes
     * @param counters     to be scheduled
     * @param needs        number needs open counter
     */
    private void scheduleBy(PassengerDistribution distribution, List<Counter> counters, int needs) {
        int temp = Math.min(counters.size(), needs);
        for (int i = 0; i < temp; i++) {
            Calendar endTime = (Calendar) distribution.startTime().clone();
            endTime.add(Calendar.MINUTE, 5);
            counters.get(i).open(distribution.startTime(), endTime);
        }
    }

    private void scheduleMustOpenCounters(List<Counter> mustOpenCounters) {
        mustOpenCounters.forEach(counter -> {
            // todo-zz statistic flights date and endTime
            String startTime = "2019-03-01 " + counter.openStartTime();
            String endTime = "2019-03-01 " + Optional.ofNullable(counter.openEndTime()).orElse(" 23:00");

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date start1 = sdf.parse(startTime);
                Calendar start2 = Calendar.getInstance();
                start2.setTime(start1);
                Date end1 = sdf.parse(endTime);
                Calendar end2 = Calendar.getInstance();
                end2.setTime(end1);

                OpenPeriod openPeriod = new OpenPeriod(start2, end2);
                counter.openPeriods().add(openPeriod);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void dateTimeOf(List<Flight> flights) {
//        flights.
    }
}

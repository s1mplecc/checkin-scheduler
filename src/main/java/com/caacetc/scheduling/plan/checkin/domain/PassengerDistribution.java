package com.caacetc.scheduling.plan.checkin.domain;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.*;

/**
 * 旅客分布区间，区间取 5min，从 00:00 ～ 24:00 的
 */
public class PassengerDistribution {
    private static final int INTERVAL = 5;
    private static NormalDistribution distribution = new NormalDistribution(90, 10);
    private List<Interval> intervals;

    public List<Interval> estimate(List<Flight> flights) {
        intervals = initIntervals(flights);
        flights.forEach(this::accumulate);
        return intervals;
    }

    /**
     * 取期望值为 90min，标准差为 10min 的正态分布，
     * 旅客从起飞前 120min 到前 60min，每间隔 5min 积分 * 该航班旅客人数，
     * 累积每个时间段的离港旅客期望
     */
    private void accumulate(Flight flight) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(flight.departTime());
        calendar.add(Calendar.HOUR_OF_DAY, -2);

        for (int i = 60; i <= 120; i += INTERVAL) {
            double premium = distribution.probability(i, i + INTERVAL) * flight.premiumCabinNum();

            double dEconomy = 0.0;
            double iEconomy = 0.0;

            if (flight.isDomestic()) {
                dEconomy = distribution.probability(i, i + INTERVAL) * flight.economyCabinNum();
            } else {
                iEconomy = distribution.probability(i, i + INTERVAL) * flight.economyCabinNum();
            }

            double finalDEconomy = dEconomy;
            double finalIEconomy = iEconomy;
            intervals.stream()
                    .filter(interval -> interval.startTime().equals(calendar))
                    .findFirst()
                    .ifPresent(interval -> interval.accumulate(premium, finalDEconomy, finalIEconomy));

            calendar.add(Calendar.MINUTE, INTERVAL);
        }
    }

    private List<Interval> initIntervals(List<Flight> flights) {
        Date start = flights.parallelStream().min(dateComparator()).map(Flight::departTime).orElseThrow();
        Date end = flights.parallelStream().max(dateComparator()).map(Flight::departTime).orElseThrow();

        List<Interval> intervals = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (calendar.getTime().before(end)) {
            intervals.add(new Interval((Calendar) calendar.clone()));
            calendar.add(Calendar.MINUTE, INTERVAL);
        }

        return intervals;
    }

    private Comparator<Flight> dateComparator() {
        return new Comparator<>() {
            @Override
            public int compare(Flight o1, Flight o2) {
                return o1.departTime().compareTo(o2.departTime());
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };
    }
}

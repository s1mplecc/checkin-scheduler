package com.caacetc.scheduling.plan.checkin;

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
        flights.forEach(flight -> accumulate(flight.departTime(), flight.premiumCabinNum(), flight.economyCabinNum()));
        return intervals;
    }

    /**
     * 取期望值为 90min，标准差为 10min 的正态分布，
     * 旅客从起飞前 120min 到前 60min，每间隔 5min 积分 * 该航班旅客人数，
     * 累积每个时间段的离港旅客期望
     */
    private void accumulate(Date departTime, Integer premiumCabinNum, Integer economyCabinNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(departTime);
        calendar.add(Calendar.HOUR, -2);

        for (int i = 60; i <= 120; i += INTERVAL) {
            long premium = Math.round(distribution.probability(i, i + INTERVAL) * premiumCabinNum);
            long economy = Math.round(distribution.probability(i, i + INTERVAL) * economyCabinNum);

            intervals.stream()
                    .filter(interval -> interval.calendar().equals(calendar))
                    .findFirst()
                    .ifPresent(interval -> interval.accumulate(premium, economy));

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

    public class Interval {
        private Calendar calendar;
        private Long premiumCabinNum;
        private Long economyCabinNum;

        Interval(Calendar calendar) {
            this(calendar, 0L, 0L);
        }

        Interval(Calendar calendar, Long premiumCabinNum, Long economyCabinNum) {
            this.calendar = calendar;
            this.premiumCabinNum = premiumCabinNum;
            this.economyCabinNum = economyCabinNum;
        }

        public Calendar calendar() {
            return calendar;
        }

        @Override
        public String toString() {
            return calendar.getTime() + " : " + premiumCabinNum + ", " + economyCabinNum;
        }

        public void accumulate(long premium, long economy) {
            this.premiumCabinNum += premium;
            this.economyCabinNum += economy;
        }
    }
}

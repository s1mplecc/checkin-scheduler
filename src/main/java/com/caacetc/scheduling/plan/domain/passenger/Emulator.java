package com.caacetc.scheduling.plan.domain.passenger;

import com.caacetc.scheduling.plan.domain.flight.Flight;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 旅客分布区间，区间取 5min
 */
@Service
public class Emulator {
    private static final int INTERVAL = 5;
    private static NormalDistribution distribution = new NormalDistribution(90, 10);

    public List<Distribution> estimateBy(List<Flight> sortedFlights) {
        List<Distribution> distributions = initIntervals(sortedFlights);
        sortedFlights.forEach(flight -> accumulate(flight, distributions));
        return distributions;
    }

    /**
     * 取期望值为 90min，标准差为 10min 的正态分布，
     * 旅客从起飞前 120min 到前 60min，每间隔 5min 积分 * 该航班旅客人数，
     * 累积每个时间段的离港旅客期望
     */
    private void accumulate(Flight flight, List<Distribution> distributions) {
        LocalDateTime flag = flight.departTime().minusHours(2);

        for (int i = 60; i <= 120; i += INTERVAL) {
            double probability = distribution.probability(i, i + INTERVAL);
            double premium = probability * flight.premiumCabinNum();
            double domEconomy = probability * flight.domEconomyCabinNum();
            double intEconomy = probability * flight.intEconomyCabinNum();

            LocalDateTime finalFlag = flag;
            distributions.parallelStream()
                    .filter(d -> d.instant().equals(finalFlag))
                    .findFirst()
                    .ifPresent(interval -> interval.accumulate(premium, domEconomy, intEconomy));

            flag = flag.plusMinutes(INTERVAL);
        }
    }

    private List<Distribution> initIntervals(List<Flight> sortedFlights) {
        LocalDateTime start = sortedFlights.get(0).departTime().minusHours(2);
        LocalDateTime end = sortedFlights.get(sortedFlights.size() - 1).departTime().minusHours(1);

        List<Distribution> distributions = new ArrayList<>();

        LocalDateTime flag = start;
        int i = 0;
        while (flag.isBefore(end)) {
            distributions.add(new Distribution(i, flag));
            flag = flag.plusMinutes(INTERVAL);
            i++;
        }

        return distributions;
    }
}

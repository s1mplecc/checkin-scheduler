package com.caacetc.scheduling.plan.domain.counter;

import java.time.Instant;
import java.time.LocalDateTime;

public class CounterDistribution {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int domEconomyNum;
    private int intEconomyNum;
    private int premiumNum;

    public CounterDistribution(Instant instant, int domEco, int intEco, int pre) {
        startTime = LocalDateTime.from(instant);
        endTime = startTime.plusHours(1);
        domEconomyNum = domEco;
        intEconomyNum = intEco;
        premiumNum = pre;
    }
}

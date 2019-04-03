package com.caacetc.scheduling.plan.domain.counter;

import java.time.LocalDateTime;

public class CounterDistribution {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int domEconomyNum;
    private int intEconomyNum;
    private int premiumNum;

    public CounterDistribution(LocalDateTime instant, int domEco, int intEco, int pre) {
        startTime = instant;
        endTime = startTime.plusHours(1);
        domEconomyNum = domEco;
        intEconomyNum = intEco;
        premiumNum = pre;
    }

    public LocalDateTime startTime() {
        return startTime;
    }

    public LocalDateTime endTime() {
        return endTime;
    }

    public int domEconomyNum() {
        return domEconomyNum;
    }

    public int intEconomyNum() {
        return intEconomyNum;
    }

    public int premiumNum() {
        return premiumNum;
    }
}

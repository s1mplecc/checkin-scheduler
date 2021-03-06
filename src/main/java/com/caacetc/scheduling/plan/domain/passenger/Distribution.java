package com.caacetc.scheduling.plan.domain.passenger;

import java.time.LocalDateTime;

public class Distribution {
    private int id;
    private LocalDateTime instant;
    private double premiumCabinNum = 0.0;
    private double dEconomyCabinNum = 0.0;
    private double iEconomyCabinNum = 0.0;

    Distribution(int id, LocalDateTime instant) {
        this.id = id;
        this.instant = instant;
    }

    public void accumulate(double premium, double dEconomy, double iEconomy) {
        this.premiumCabinNum += premium;
        this.dEconomyCabinNum += dEconomy;
        this.iEconomyCabinNum += iEconomy;
    }

    public int id() {
        return id;
    }

    public int domEconomyCounters() {
        double v = dEconomyCabinNum * 0.95 * 0.91 * (0.26 + 0.74 * 0.25);
        return (int) Math.ceil(v * 60 / 300);
    }

    public int intEconomyCounters() {
        double v = iEconomyCabinNum * 0.95 * 0.55;
        return (int) Math.ceil(v * 90 / 300);
    }

    public int premiumCounters() {
        return (int) Math.ceil(premiumCabinNum * 0.95 * 0.55 * 120 / 300);
    }

    public int premiumCabinNum() {
        return (int) premiumCabinNum;
    }

    public int dEconomyCabinNum() {
        return (int) dEconomyCabinNum;
    }

    public int iEconomyCabinNum() {
        return (int) iEconomyCabinNum;
    }

    public LocalDateTime instant() {
        return instant;
    }
}

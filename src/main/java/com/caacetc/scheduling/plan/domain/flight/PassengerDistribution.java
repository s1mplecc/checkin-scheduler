package com.caacetc.scheduling.plan.domain.flight;

import java.util.Calendar;

public class PassengerDistribution {
    private Calendar time;
    private double premiumCabinNum;
    private double dEconomyCabinNum;
    private double iEconomyCabinNum;

    PassengerDistribution(Calendar time) {
        this(time, 0.0, 0.0, 0.0);
    }

    PassengerDistribution(Calendar time, double premiumCabinNum, double dEconomyCabinNum, double iEconomyCabinNum) {
        this.time = time;
        this.premiumCabinNum = premiumCabinNum;
        this.dEconomyCabinNum = dEconomyCabinNum;
        this.iEconomyCabinNum = iEconomyCabinNum;
    }

    public int dEconomyCounters() {
        double v = dEconomyCabinNum * 0.95 * 0.91 * (0.26 + 0.74 * 0.25);
        return (int) (v * 60 / 300);
    }

    public int iEconomyCounters() {
        double v = iEconomyCabinNum * 0.95 * 0.55;
        return (int) (v * 90 / 300);
    }

    public int premiumCounters() {
        return (int) (premiumCabinNum * 0.95 * 0.55 * 120 / 300);
    }

    public Calendar startTime() {
        return time;
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

    @Override
    public String toString() {
        return time.getTime() + " : " +
                premiumCounters() + ", " +
                dEconomyCounters() + ", " +
                iEconomyCounters();
    }

    public void accumulate(double premium, double dEconomy, double iEconomy) {
        this.premiumCabinNum += premium;
        this.dEconomyCabinNum += dEconomy;
        this.iEconomyCabinNum += iEconomy;
    }
}

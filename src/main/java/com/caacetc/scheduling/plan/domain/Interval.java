package com.caacetc.scheduling.plan.domain;

import java.util.Calendar;

public class Interval {
    private Calendar startTime;
    private double premiumCabinNum; // 高端旅客人数
    private double dEconomyCabinNum; // 国内经济舱旅客人数
    private double iEconomyCabinNum; // 国际经济舱旅客人数

    /**
     * 国内经济舱人工办理柜台, 60s/人
     */
    private int dEconomyCounters;

    /**
     * 国际经济舱人工办理柜台, 90s/人
     */
    private int iEconomyCounters;

    /**
     * 国航高端值机柜台, 120s/人
     */
    private int premiumCounters;

    Interval(Calendar startTime) {
        this(startTime, 0.0, 0.0, 0.0);
    }

    Interval(Calendar startTime, double premiumCabinNum, double dEconomyCabinNum, double iEconomyCabinNum) {
        this.startTime = startTime;
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
        return startTime;
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
        StringBuilder sb = new StringBuilder();
        sb.append(startTime.getTime()).append(" : ")
                .append(premiumCounters()).append(", ")
                .append(dEconomyCounters()).append(", ")
                .append(iEconomyCounters());
        return sb.toString();
    }

    public void accumulate(double premium, double dEconomy, double iEconomy) {
        this.premiumCabinNum += premium;
        this.dEconomyCabinNum += dEconomy;
        this.iEconomyCabinNum += iEconomy;
    }
}

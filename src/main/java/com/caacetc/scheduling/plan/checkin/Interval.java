package com.caacetc.scheduling.plan.checkin;

import java.util.Calendar;

public class Interval {
    private Calendar calendar;
    private double premiumCabinNum; // 高端旅客人数
    private double dEconomyCabinNum; // 国内经济舱旅客人数
    private double iEconomyCabinNum; // 国际经济舱旅客人数

    Interval(Calendar calendar) {
        this(calendar, 0.0, 0.0, 0.0);
    }

    Interval(Calendar calendar, double premiumCabinNum, double dEconomyCabinNum, double iEconomyCabinNum) {
        this.calendar = calendar;
        this.premiumCabinNum = premiumCabinNum;
        this.dEconomyCabinNum = dEconomyCabinNum;
        this.iEconomyCabinNum = iEconomyCabinNum;
    }

    public Calendar calendar() {
        return calendar;
    }

    public long premiumCabinNum() {
        return (long) premiumCabinNum;
    }

    public long dEconomyCabinNum() {
        return (long) dEconomyCabinNum;
    }

    public long iEconomyCabinNum() {
        return (long) iEconomyCabinNum;
    }

    @Override
    public String toString() {
        return calendar.getTime() + " : " + premiumCabinNum() + ", " + dEconomyCabinNum() + ", " + iEconomyCabinNum();
    }

    public void accumulate(double premium, double dEconomy, double iEconomy) {
        this.premiumCabinNum += premium;
        this.dEconomyCabinNum += dEconomy;
        this.iEconomyCabinNum += iEconomy;
    }
}

package com.caacetc.scheduling.plan.controllers.response;

import com.caacetc.scheduling.plan.domain.Interval;
import lombok.Data;

import java.util.Date;

@Data
public class PassengerDistributionRes {
    private Date date;
    /**
     * 高端旅客人数
     */
    private int premiumCabinNum;
    /**
     * 国内经济舱旅客人数
     */
    private int dEconomyCabinNum;
    /**
     * 国际经济舱旅客人数
     */
    private int iEconomyCabinNum;
    /**
     * 需要开放的国内经济舱人工办理柜台数, 60s/人
     */
    private int dEconomyCounters;
    /**
     * 需要开放的国际经济舱人工办理柜台数, 90s/人
     */
    private int iEconomyCounters;
    /**
     * 需要开放的国航高端值机柜台数, 120s/人
     */
    private int premiumCounters;

    public PassengerDistributionRes(Interval interval) {
        date = interval.startTime().getTime();
        dEconomyCabinNum = interval.dEconomyCabinNum();
        iEconomyCabinNum = interval.iEconomyCabinNum();
        premiumCabinNum = interval.premiumCabinNum();
        dEconomyCounters = interval.dEconomyCounters();
        iEconomyCounters = interval.iEconomyCounters();
        premiumCounters = interval.premiumCounters();
    }
}

package com.caacetc.scheduling.plan.controllers.response;

import com.caacetc.scheduling.plan.domain.flight.PassengerDistribution;
import lombok.Data;

import java.util.Date;

@Data
public class PassengerDistributionResponse {
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

    public PassengerDistributionResponse(PassengerDistribution passengerDistribution) {
        date = passengerDistribution.startTime().getTime();
        dEconomyCabinNum = passengerDistribution.dEconomyCabinNum();
        iEconomyCabinNum = passengerDistribution.iEconomyCabinNum();
        premiumCabinNum = passengerDistribution.premiumCabinNum();
        dEconomyCounters = passengerDistribution.domEconomyCounters();
        iEconomyCounters = passengerDistribution.intEconomyCounters();
        premiumCounters = passengerDistribution.premiumCounters();
    }
}

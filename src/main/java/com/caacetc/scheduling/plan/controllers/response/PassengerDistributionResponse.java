package com.caacetc.scheduling.plan.controllers.response;

import com.caacetc.scheduling.plan.domain.passenger.Distribution;
import lombok.Data;

import java.time.ZoneId;
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

    public PassengerDistributionResponse(Distribution distribution) {
        date = Date.from(distribution.instant().atZone(ZoneId.systemDefault()).toInstant());
        dEconomyCabinNum = distribution.dEconomyCabinNum();
        iEconomyCabinNum = distribution.iEconomyCabinNum();
        premiumCabinNum = distribution.premiumCabinNum();
        dEconomyCounters = distribution.domEconomyCounters();
        iEconomyCounters = distribution.intEconomyCounters();
        premiumCounters = distribution.premiumCounters();
    }
}

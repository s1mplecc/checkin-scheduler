package com.caacetc.scheduling.plan.checkin;

import lombok.ToString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ToString
public class Flight {
    private final String date;
    private final String departTime;
    private final String passengerNum;
    private final String region;

    private Integer economyCabinNum;
    private Integer premiumCabinNum;

    public Flight(String date, String departTime, String passengerNum, String region) {
        this.date = date;
        this.departTime = departTime;
        this.passengerNum = passengerNum;
        this.region = region;
        this.economyCabinNum = 0;
        this.premiumCabinNum = 0;

        parseCabinNumsBy(passengerNum);
    }

    public void parseCabinNumsBy(String passengerNum) {
        for (CabinRank rank : CabinRank.values()) {
            Pattern compile = rank.pattern();
            Matcher matcher = compile.matcher(passengerNum);
            while (matcher.find()) {
                Integer adultsNum = Integer.valueOf(matcher.group(2));
                Integer childrenNum = Integer.valueOf(matcher.group(3));
                Integer babiesNum = Integer.valueOf(matcher.group(4));

                if (rank.isEconomy()) {
                    economyCabinNum += adultsNum + childrenNum + babiesNum;
                } else {
                    premiumCabinNum += adultsNum + childrenNum + babiesNum;
                }
            }
        }
    }
}

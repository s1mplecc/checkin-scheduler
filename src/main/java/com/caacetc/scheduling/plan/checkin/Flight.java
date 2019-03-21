package com.caacetc.scheduling.plan.checkin;

import lombok.ToString;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ToString
public class Flight {
    private final String date;
    private final String passengerNum;
    private final String region;
    private final String destination;
    private Date departTime;

    private Integer economyCabinNum;
    private Integer premiumCabinNum;

    public Date departTime() {
        return departTime;
    }

    public Integer economyCabinNum() {
        return economyCabinNum;
    }

    public Integer premiumCabinNum() {
        return premiumCabinNum;
    }

    public Flight(String date, String departTime, String passengerNum, String region, String destination) {
        this.date = date;
        this.passengerNum = passengerNum;
        this.region = region;
        this.destination = destination;
        this.economyCabinNum = 0;
        this.premiumCabinNum = 0;
        try {
            this.departTime = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss").parse(departTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        computeCabinNumsBy(passengerNum);
    }

    public void computeCabinNumsBy(String passengerNum) {
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

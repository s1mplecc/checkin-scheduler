package com.caacetc.scheduling.plan.checkin;

import lombok.ToString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ToString
public class Flight {
    private String date;
    private String departTime;
    private String passengerNum;
    private String region;

    private Integer economyClassNum;
    private Integer premiumNum;

    public Flight(String date, String departTime, String passengerNum, String region) {
        this.date = date;
        this.departTime = departTime;
        this.passengerNum = passengerNum;
        this.region = region;

        parse(passengerNum);
    }

    public void parse(String passengerNum) {
        Pattern compile = Pattern.compile(".*\\[Y:(\\d+),(\\d+),(\\d+)\\].*");
        Matcher matcher = compile.matcher(passengerNum);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            System.out.println(matcher.group(3));
        }
    }
}

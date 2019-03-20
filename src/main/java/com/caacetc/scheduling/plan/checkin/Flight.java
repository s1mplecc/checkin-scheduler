package com.caacetc.scheduling.plan.checkin;

public class Flight {
    private String date;
    private String departTime;
    private String passengerNum;
    private String region;

    public Flight(String date, String departTime, String passengerNum, String region) {
        this.date = date;
        this.departTime = departTime;
        this.passengerNum = passengerNum;
        this.region = region;
    }
}

package com.caacetc.scheduling.plan.controllers.request;

import lombok.Data;

@Data
public class FlightReq {
    private Long id;
    private String departTime;
    private String region;
    private Integer economyCabinNum;
    private Integer premiumCabinNum;

    public FlightReq(FlightReq flightReq) {

    }
}

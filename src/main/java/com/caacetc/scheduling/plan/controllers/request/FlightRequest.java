package com.caacetc.scheduling.plan.controllers.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FlightRequest {
    private Long id;
    private String departTime;
    private String region;
    private Integer economyCabinNum;
    private Integer premiumCabinNum;
}

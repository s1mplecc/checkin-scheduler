package com.caacetc.scheduling.plan.controllers.request;

import com.caacetc.scheduling.plan.domain.flight.Region;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FlightRequest {
    private long id;
    private String departTime;
    private Region region;
    private int economyCabinNum;
    private int premiumCabinNum;
}

package com.caacetc.scheduling.plan.controllers.response;

import com.caacetc.scheduling.plan.domain.counter.Counter;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CounterResponse {
    private final String id;
    private final String region;
    private final String type;
    private final List<OpenPeriodResponse> openPeriods;

    public CounterResponse(Counter counter) {
        id = counter.id();
        region = counter.region();
        type = counter.type();
        openPeriods = counter.openPeriods().stream().map(OpenPeriodResponse::new).collect(Collectors.toList());
    }
}

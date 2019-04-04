package com.caacetc.scheduling.plan.controllers.response;

import com.caacetc.scheduling.plan.domain.counter.Counter;
import com.caacetc.scheduling.plan.domain.counter.OpenPeriod;
import lombok.Data;

import java.util.List;

@Data
public class CounterResponse {
    private final String code;
    private final String region;
    private final String type;
    private final List<OpenPeriod> openPeriods;

    public CounterResponse(Counter counter) {
        code = counter.code();
        region = counter.region();
        type = counter.type();
        openPeriods = counter.openPeriods();
    }
}

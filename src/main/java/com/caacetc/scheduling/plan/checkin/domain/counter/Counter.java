package com.caacetc.scheduling.plan.checkin.domain.counter;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class Counter {
    private final String id;
    private final String region;
    private final String type;
    private List<OpenPeriod> openPeriods;

    public Counter(String id, String region, String type) {
        this.id = id;
        this.region = region;
        this.type = type;
        this.openPeriods = new ArrayList<>();
    }

    public void addOpenPeriods(OpenPeriod openPeriod) {
        openPeriods.add(openPeriod);
    }

    public String id() {
        return id;
    }

    public String region() {
        return region;
    }

    public String type() {
        return type;
    }
}

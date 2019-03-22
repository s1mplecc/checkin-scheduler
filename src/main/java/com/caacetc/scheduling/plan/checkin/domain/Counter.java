package com.caacetc.scheduling.plan.checkin.domain;

import lombok.ToString;

@ToString
public class Counter {
    private final String id;
    private final String region;
    private final String type;

    public Counter(String id, String region, String type) {
        this.id = id;
        this.region = region;
        this.type = type;
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

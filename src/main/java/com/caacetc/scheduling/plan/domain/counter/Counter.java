package com.caacetc.scheduling.plan.domain.counter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Counter implements Comparable<Counter> {
    private final String code;
    private final String region;
    private final String type;
    private final List<OpenPeriod> openPeriods;
    private final int isMustOpen;
    private final String openStartTime;
    private final String openEndTime;

    public Counter(String code, String region, String type, int isMustOpen, String openStartTime, String openEndTime) {
        this.code = code;
        this.region = region;
        this.type = type;
        this.isMustOpen = isMustOpen;
        this.openStartTime = openStartTime;
        this.openEndTime = openEndTime;
        this.openPeriods = new ArrayList<>();
    }

    public void open(LocalDateTime startTime) {
        // todo-zz
    }

    public String openStartTime() {
        return openStartTime;
    }

    public String openEndTime() {
        return openEndTime;
    }

    public boolean mustOpen() {
        return isMustOpen == 1;
    }

    public List<OpenPeriod> openPeriods() {
        return openPeriods;
    }

    public boolean isPremium() {
        return "高端".equals(type);
    }

    public boolean isEconomy() {
        return "经济".equals(type);
    }

    public boolean isInt() {
        return "I".equals(region);
    }

    public boolean isDom() {
        return "D".equals(region);
    }

    public boolean isDomAndInt() {
        return "D/I".equals(region);
    }

    public String code() {
        return code;
    }

    public String region() {
        return region;
    }

    public String type() {
        return type;
    }

    @Override
    public int compareTo(Counter o) {
        return code.compareTo(o.code);
    }
}

package com.caacetc.scheduling.plan.domain.counter;

import java.time.LocalDateTime;
import java.util.List;

public class Counter implements Comparable<Counter> {

    //-------------------
    // Load From DateBase
    //-------------------

    private final String code;
    private final String region;
    private final String type;
    private final int isMustOpen;
    private final String openStartTime;
    private final String openEndTime;

    //--------------------------------
    // Schedule Counter's Open Periods
    //--------------------------------

    private final OpenPeriod openPeriod;

    public Counter(String code, String region, String type, int isMustOpen, String openStartTime, String openEndTime) {
        this.code = code;
        this.region = region;
        this.type = type;
        this.isMustOpen = isMustOpen;
        this.openStartTime = openStartTime;
        this.openEndTime = openEndTime;
        openPeriod = new OpenPeriod();
    }

    public void open(LocalDateTime startTime, LocalDateTime endTime) {
        OpenFragment openFragment = new OpenFragment(code, startTime, endTime);
        openPeriod.add(openFragment);
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

    public List<OpenFragment> openPeriods() {
        return openPeriod.openFragments();
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

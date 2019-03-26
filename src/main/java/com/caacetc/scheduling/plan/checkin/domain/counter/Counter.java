package com.caacetc.scheduling.plan.checkin.domain.counter;

import lombok.ToString;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public List<OpenPeriod> openPeriods() {
        return openPeriods;
    }

    public void open(Calendar startTime, Calendar endTime) {
        AtomicBoolean isAppendOrPropel = new AtomicBoolean(false);

        openPeriods.stream()
                .filter(openPeriod -> openPeriod.endTime().equals(startTime))
                .findFirst()
                .ifPresent(openPeriod -> {
                    openPeriod.append(endTime);
                    isAppendOrPropel.set(true);
                });

        openPeriods.stream()
                .filter(openPeriod -> openPeriod.startTime().equals(endTime))
                .findFirst()
                .ifPresent(openPeriod -> {
                    openPeriod.propel(startTime);
                    isAppendOrPropel.set(true);
                });

        if (!isAppendOrPropel.get()) {
            openPeriods.add(new OpenPeriod(startTime, endTime));
        }
    }

    public void addOpenPeriod(OpenPeriod openPeriod) {
        if (openPeriod.isGt3Hours()) {
            openPeriods.addAll(openPeriod.split());
        } else {
            openPeriods.add(openPeriod);
        }
    }

    public boolean isPrem() {
        return "高端".equals(type);
    }

    public boolean isEcon() {
        return "经济".equals(type);
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

package com.caacetc.scheduling.plan.domain.counter;

import lombok.ToString;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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

    public List<OpenPeriod> openPeriodsAfterSplit() {
        List<OpenPeriod> openPeriods = openPeriods();

        List<OpenPeriod> result = openPeriods.stream()
                .filter(openPeriod -> !openPeriod.gt3Hours())
                .collect(Collectors.toList());

        openPeriods.stream()
                .filter(OpenPeriod::gt3Hours)
                .forEach(openPeriod -> result.addAll(openPeriod.split()));

        return result;
    }

    /**
     * combine if two intervals less than 15min;
     * abandon if intervals continue less than 1hour
     */
    public List<OpenPeriod> openPeriods() {
        List<OpenPeriod> result = new ArrayList<>();

        openPeriods.stream()
                .sorted()
                .forEach(openPeriod -> {
                    if (result.isEmpty()) {
                        result.add(openPeriod);
                    }

                    OpenPeriod lastOne = result.get(result.size() - 1);
                    if (lastOne.only15minutesAfter(openPeriod)) {
                        openPeriod.combineWith(lastOne);
                    } else {
                        result.add(openPeriod);
                    }
                });

        return openPeriods.stream()
                .filter(OpenPeriod::longerThan1Hour)
                .collect(Collectors.toList());
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

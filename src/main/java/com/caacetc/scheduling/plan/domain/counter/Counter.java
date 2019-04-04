package com.caacetc.scheduling.plan.domain.counter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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

    public List<OpenPeriod> openPeriodsAfterSplit() {
        List<OpenPeriod> openPeriods = openPeriodsAfterGoverning();

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
    public List<OpenPeriod> openPeriodsAfterGoverning() {
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
            openPeriods.add(new OpenPeriod(code, startTime, endTime));
        }
    }

    public boolean isPremuim() {
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

    public boolean isDomesticAndInternational() {
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

    public void clear() {
        openPeriods.clear();
    }

    @Override
    public int compareTo(Counter o) {
        return code.compareTo(o.code);
    }

    // todo-zz
    public void open(LocalDateTime startTime) {

    }
}

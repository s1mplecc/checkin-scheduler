package com.caacetc.scheduling.plan.checkin.domain.staff;

import java.util.Calendar;

public class Workplan {
    private final Calendar startTime;
    private final Calendar endTime;

    public Workplan(Calendar startTime, Calendar endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Calendar startTime() {
        return startTime;
    }

    public Calendar mondayThisWeek() {
        Calendar monday = (Calendar) startTime.clone();
        int i = -monday.get(Calendar.DAY_OF_WEEK) + 2;
        monday.roll(Calendar.DAY_OF_WEEK, i);
        monday.set(Calendar.HOUR, 0);
        monday.set(Calendar.MINUTE, 0);
        monday.set(Calendar.SECOND, 0);
        return monday;
    }
}

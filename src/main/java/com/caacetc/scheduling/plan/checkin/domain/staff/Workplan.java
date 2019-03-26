package com.caacetc.scheduling.plan.checkin.domain.staff;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Workplan {
    private final Calendar startTime;
    private final Calendar endTime;

    public Workplan(Calendar startTime) {
        this.startTime = startTime;
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 8);
        this.endTime = endTime;
    }

    Workplan(Calendar startTime, Calendar endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Calendar endTime() {
        return endTime;
    }

    public Calendar startTime() {
        return startTime;
    }

    public Calendar mondayThisWeek() {
        Calendar monday = (Calendar) startTime.clone();
        monday.setFirstDayOfWeek(Calendar.MONDAY);
        int i = -monday.get(Calendar.DAY_OF_WEEK) + 2;
        if (i == 1) {
            monday.roll(Calendar.DAY_OF_WEEK, -6);
        } else {
            monday.roll(Calendar.DAY_OF_WEEK, i);
        }
        monday.set(Calendar.HOUR_OF_DAY, 0);
        monday.set(Calendar.MINUTE, 0);
        monday.set(Calendar.SECOND, 0);
        return monday;
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("MM-dd HH:mm").format(startTime.getTime())
                + " ~ " +
                new SimpleDateFormat("MM-dd HH:mm").format(endTime.getTime());
    }
}

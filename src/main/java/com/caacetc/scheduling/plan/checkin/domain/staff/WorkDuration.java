package com.caacetc.scheduling.plan.checkin.domain.staff;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 员工工作类，包含上班时间、下班时间、工作在柜台的时间片段
 */
public class WorkDuration {
    private final Calendar onDuty;
    private final Calendar offDuty;

    public WorkDuration(Calendar onDuty) {
        this.onDuty = onDuty;
        Calendar endTime = (Calendar) onDuty.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 8);
        this.offDuty = endTime;
    }

    public Calendar endTime() {
        return offDuty;
    }

    public Calendar startTime() {
        return onDuty;
    }

    public Calendar mondayThisWeek() {
        Calendar monday = (Calendar) onDuty.clone();
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
        return new SimpleDateFormat("MM-dd HH:mm").format(onDuty.getTime())
                + " ~ " +
                new SimpleDateFormat("MM-dd HH:mm").format(offDuty.getTime());
    }
}

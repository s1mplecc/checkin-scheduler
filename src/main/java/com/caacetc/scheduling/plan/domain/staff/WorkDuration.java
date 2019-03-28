package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.OpenPeriod;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 员工工作类，包含上班时间、下班时间、工作在柜台的时间片段
 */
public class WorkDuration {
    @Getter
    private final Calendar onDuty;
    @Getter
    private final Calendar offDuty;
    private final List<OpenPeriod> workPeriods;

    public WorkDuration(Calendar onDuty) {
        this.onDuty = onDuty;
        Calendar endTime = (Calendar) onDuty.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 8);
        this.offDuty = endTime;
        this.workPeriods = new ArrayList<>();
    }

    public void add(OpenPeriod workPeriod) {
        workPeriods.add(workPeriod);
    }

    public Calendar offDuty() {
        return offDuty;
    }

    public Calendar onDuty() {
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

package com.caacetc.scheduling.plan.checkin.domain.staff;

import com.caacetc.scheduling.plan.checkin.domain.counter.OpenPeriod;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@ToString
public class Agenda {
    private List<WorkDuration> workDurations;

    public Agenda() {
        workDurations = new ArrayList<>();
    }

    public void add(OpenPeriod openPeriod) {
        boolean exist = false;

        for (WorkDuration workDuration : workDurations) {
            if (workDuration.onDuty().get(Calendar.DATE) == openPeriod.startTime().get(Calendar.DATE)) {
                workDuration.add(openPeriod);
                exist = true;
                break;
            }
        }

        if (!exist) {
            workDurations.add(new WorkDuration(openPeriod.startTime()));
        }
    }

    public WorkDuration workDurationOf(OpenPeriod openPeriod) {
        return workDurations.stream()
                .filter(workDuration -> workDuration.onDuty().get(Calendar.DATE) == openPeriod.startTime().get(Calendar.DATE))
                .findFirst()
                .orElse(null);
    }

    public boolean inWorkDuration(OpenPeriod openPeriod) {
        WorkDuration workDuration = workDurationOf(openPeriod);
        if (workDuration == null) {
            return true;
        }
        return !openPeriod.endTime().after(workDuration.offDuty());
    }

    public boolean oneWeekLte5Days(OpenPeriod openPeriod) {
        WorkDuration workDuration = new WorkDuration(openPeriod.startTime());
        Calendar mondayThisWeek = workDuration.mondayThisWeek();
        return workDurations.stream()
                .filter(wp -> !wp.onDuty().before(mondayThisWeek))
                .count() <= 4;
    }

    public boolean mostlyContinue4Days(OpenPeriod openPeriod) {
        return workDurations.stream()
                .filter(wp -> {
                    int intervalDate = openPeriod.startTime().get(Calendar.DATE) - wp.onDuty().get(Calendar.DATE);
                    return intervalDate <= 4;
                })
                .count() <= 4;
    }

    public boolean lastIntervalGt12Hours(OpenPeriod openPeriod) {
        return workDurations.stream()
                .map(wp -> openPeriod.startTime().getTime().getTime() - wp.offDuty().getTime().getTime())
                .allMatch(interval -> interval >= 1000 * 60 * 60 * 12);
    }

    public int workHours() {
        return workDurations.size() * 8;
    }

    public List<WorkDuration> workplans() {
        return workDurations;
    }
}

package com.caacetc.scheduling.plan.checkin.domain.staff;

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

    public void add(WorkDuration workPlan) {
        workDurations.add(workPlan);
    }

    public List<WorkDuration> workplans() {
        return workDurations;
    }

    public boolean oneWeekLte5Days(WorkDuration workDuration) {
        Calendar mondayThisWeek = workDuration.mondayThisWeek();
        return workDurations.stream()
                .filter(wp -> !wp.startTime().before(mondayThisWeek))
                .count() <= 4;
    }

    public boolean mostlyContinue4Days(WorkDuration workDuration) {
        return workDurations.stream()
                .filter(wp -> {
                    int intervalDate = workDuration.startTime().get(Calendar.DATE) - wp.startTime().get(Calendar.DATE);
                    return intervalDate <= 4;
                })
                .count() <= 4;
    }

    public boolean lastIntervalGt12Hours(WorkDuration workDuration) {
        return workDurations.stream()
                .map(wp -> workDuration.startTime().getTime().getTime() - wp.endTime().getTime().getTime())
                .allMatch(interval -> interval >= 1000 * 60 * 60 * 12);
    }

    public int workHours() {
        return workDurations.size() * 8;
    }
}

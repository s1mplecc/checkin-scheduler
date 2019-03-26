package com.caacetc.scheduling.plan.checkin.domain.staff;

import lombok.ToString;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@ToString
public class Agenda {
    private List<Workplan> workplans;

    public Agenda() {
        workplans = new ArrayList<>();
    }

    public void add(Workplan workPlan) {
        workplans.add(workPlan);
    }

    public List<Workplan> workplans() {
        return workplans;
    }

    public boolean oneWeekLte5Days(Workplan workplan) {
        Calendar mondayThisWeek = workplan.mondayThisWeek();
        return workplans.stream()
                .filter(wp -> !wp.startTime().before(mondayThisWeek))
                .count() <= 4;
    }

    public boolean mostlyContinue4Days(Workplan workplan) {
        return workplans.stream()
                .filter(wp -> {
                    int intervalDate = workplan.startTime().get(Calendar.DATE) - wp.startTime().get(Calendar.DATE);
                    return intervalDate <= 4;
                })
                .count() <= 4;
    }

    public boolean lastIntervalGt12Hours(Workplan workplan) {
        return workplans.stream()
                .map(wp -> workplan.startTime().getTime().getTime() - wp.endTime().getTime().getTime())
                .allMatch(interval -> interval >= 1000 * 60 * 60 * 12);
    }

    public int workHours() {
        return workplans.size() * 8;
    }
}

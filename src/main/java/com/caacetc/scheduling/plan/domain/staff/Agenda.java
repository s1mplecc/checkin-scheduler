package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.OpenFragment;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
public class Agenda {
    private List<WorkDay> workDays;

    public Agenda() {
        workDays = new ArrayList<>();
    }

    public void add(OpenFragment openFragment) {
//        boolean exist = false;
//
//        for (WorkDay workDuration : workDays) {
//            if (workDuration.onDuty().get(Calendar.DATE) == openFragment.startTime().get(Calendar.DATE)) {
//                workDuration.add(openFragment);
//                exist = true;
//                break;
//            }
//        }
//
//        if (!exist) {
//            WorkDay workDuration = new WorkDay(openFragment.startTime());
//            workDuration.add(openFragment);
//            workDays.add(workDuration);
//        }
    }

    public WorkDay workDurationOf(OpenFragment openFragment) {
        return workDays.stream()
                .filter(workDay -> workDay.onDuty().toLocalDate().isEqual(openFragment.startTime().toLocalDate()))
                .findFirst()
                .orElse(null);
    }

    public boolean inWorkDuration(OpenFragment openFragment) {
        return true;
//        WorkDay workDuration = workDurationOf(openFragment);
//        if (workDuration == null) {
//            return true;
//        }
//        return !openFragment.endTime().after(workDuration.offDuty());
    }

    public boolean oneWeekLte5Days(OpenFragment openFragment) {
        WorkDay workDay = new WorkDay(openFragment.startTime());
        LocalDateTime mondayThisWeek = workDay.mondayThisWeek();
        return workDays.stream()
                .filter(wp -> !wp.onDuty().isBefore(mondayThisWeek))
                .count() <= 4;
    }

    public boolean mostlyContinue4Days(OpenFragment openFragment) {
        return true;
//        return workDays.stream()
//                .filter(wp -> {
//                    int intervalDate = openFragment.startTime().get(Calendar.DATE) - wp.onDuty().get(Calendar.DATE);
//                    return intervalDate <= 4;
//                })
//                .count() <= 4;
    }

    public boolean lastIntervalGt12Hours(OpenFragment openFragment) {
        return true;
//        return workDays.stream()
//                .map(wp -> openFragment.startTime().getTime().getTime() - wp.offDuty().getTime().getTime())
//                .allMatch(interval -> interval >= 1000 * 60 * 60 * 12);
    }

    public int workDays() {
        return workDays.size();
    }

    public List<WorkDay> workplans() {
        return workDays;
    }
}

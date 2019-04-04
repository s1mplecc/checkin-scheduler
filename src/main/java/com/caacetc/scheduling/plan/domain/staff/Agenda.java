package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.OpenFragment;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
public class Agenda {
    private List<WorkDuration> workDurations;

    public Agenda() {
        workDurations = new ArrayList<>();
    }

    public void add(OpenFragment openFragment) {
//        boolean exist = false;
//
//        for (WorkDuration workDuration : workDurations) {
//            if (workDuration.onDuty().get(Calendar.DATE) == openFragment.startTime().get(Calendar.DATE)) {
//                workDuration.add(openFragment);
//                exist = true;
//                break;
//            }
//        }
//
//        if (!exist) {
//            WorkDuration workDuration = new WorkDuration(openFragment.startTime());
//            workDuration.add(openFragment);
//            workDurations.add(workDuration);
//        }
    }

    public WorkDuration workDurationOf(OpenFragment openFragment) {
        return workDurations.stream()
                .filter(workDuration -> workDuration.onDuty().toLocalDate().isEqual(openFragment.startTime().toLocalDate()))
                .findFirst()
                .orElse(null);
    }

    public boolean inWorkDuration(OpenFragment openFragment) {
        return true;
//        WorkDuration workDuration = workDurationOf(openFragment);
//        if (workDuration == null) {
//            return true;
//        }
//        return !openFragment.endTime().after(workDuration.offDuty());
    }

    public boolean oneWeekLte5Days(OpenFragment openFragment) {
        WorkDuration workDuration = new WorkDuration(openFragment.startTime());
        LocalDateTime mondayThisWeek = workDuration.mondayThisWeek();
        return workDurations.stream()
                .filter(wp -> !wp.onDuty().isBefore(mondayThisWeek))
                .count() <= 4;
    }

    public boolean mostlyContinue4Days(OpenFragment openFragment) {
        return true;
//        return workDurations.stream()
//                .filter(wp -> {
//                    int intervalDate = openFragment.startTime().get(Calendar.DATE) - wp.onDuty().get(Calendar.DATE);
//                    return intervalDate <= 4;
//                })
//                .count() <= 4;
    }

    public boolean lastIntervalGt12Hours(OpenFragment openFragment) {
        return true;
//        return workDurations.stream()
//                .map(wp -> openFragment.startTime().getTime().getTime() - wp.offDuty().getTime().getTime())
//                .allMatch(interval -> interval >= 1000 * 60 * 60 * 12);
    }

    public int workHours() {
        return workDurations.size() * 8;
    }

    public List<WorkDuration> workplans() {
        return workDurations;
    }
}

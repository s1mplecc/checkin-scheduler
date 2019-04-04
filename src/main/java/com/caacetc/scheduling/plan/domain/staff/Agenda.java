package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.OpenFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Agenda {
    private List<WorkDay> workDays;

    public Agenda() {
        workDays = new ArrayList<>();
    }

    public Optional<WorkDay> existWorkDay(LocalDate date) {
        return workDays.stream()
                .filter(workDay -> workDay.date().isEqual(date))
                .findFirst();
    }

    public void add(OpenFragment task) {
        AtomicBoolean exist = new AtomicBoolean(false);
        existWorkDay(task.startTime().toLocalDate())
                .ifPresent(workDay -> {
                    exist.set(true);
                    workDay.add(task);
                });
        if (!exist.get()) {
            WorkDay newWorkDay = new WorkDay(task.startTime());
            workDays.add(newWorkDay);
        }
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

    public int workDaysNum() {
        return workDays.size();
    }

    public List<WorkDay> workplans() {
        return workDays;
    }
}

package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.OpenFragment;
import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

public class Agenda {
    private List<WorkDay> workDays;

    public Agenda() {
        workDays = new ArrayList<>();
    }

    public void add(OpenFragment task) {
        AtomicBoolean exist = new AtomicBoolean(false);
        workDays.stream()
                .filter(workDay -> workDay.canIncludeTask(task))
                .findFirst()
                .ifPresent(workDay -> {
                    exist.set(true);
                    workDay.add(task);
                });
        if (!exist.get()) {
            WorkDay newWorkDay = new WorkDay(task.startTime());
            newWorkDay.add(task);
            workDays.add(newWorkDay);
        }
    }

    public boolean isLegal(OpenFragment task) {
        if (workDays.isEmpty()) {
            return true;
        }

        if (workDays.stream().anyMatch(workDay -> workDay.canIncludeTask(task))) {
            return true;
        }

        // it means should new one work day to assign
        if (workDays.stream().anyMatch(workDay -> workDay.date().isEqual(task.date()))) {
            return false;
        }

        // one week less and equal than 5 days
        WorkDay w = new WorkDay(task.startTime());
        LocalDateTime thisMonday = w.mondayThisWeek();
        LocalDateTime nextMonday = thisMonday.plusWeeks(1);
        if (workDays.stream()
                .filter(workDay ->
                        !workDay.date().isBefore(thisMonday.toLocalDate())
                                && workDay.date().isBefore(nextMonday.toLocalDate()))
                .count() >= 5) {
            return false;
        }

        // interval work hours greater and equal than 12 hours
        Optional<WorkDay> before = workDays.stream()
                .filter(workDay -> workDay.date().isBefore(task.date()))
                .max(Comparator.comparing(WorkDay::date));
        if (before.isPresent()) {
            if (Math.abs(before.get().offDuty().until(task.startTime(), HOURS)) < 12) {
                return false;
            }
        }
        Optional<WorkDay> after = workDays.stream()
                .filter(workDay -> workDay.date().isAfter(task.date()))
                .min(Comparator.comparing(WorkDay::date));
        if (after.isPresent()) {
            if (Math.abs(after.get().onDuty().until(task.endTime(), HOURS)) < 12) {
                return false;
            }
        }

        List<WorkDay> temp = Lists.newArrayList();
        temp.addAll(workDays);
        temp.add(w);
        List<WorkDay> sortedWorkDays = temp.stream().sorted().collect(Collectors.toList());
        int continueDays = 0;
        for (int i = 0; i < sortedWorkDays.size() - 1; i++) {
            if (sortedWorkDays.get(i).date().until(sortedWorkDays.get(i + 1).date(), DAYS) == 1) {
                continueDays += 1;
                if (continueDays >= 4) {
                    return false;
                }
            } else {
                continueDays = 0;
            }
        }

        return true;
    }

    public int workDaysNum() {
        return workDays.size();
    }

    public List<WorkDay> workplans() {
        return workDays;
    }
}

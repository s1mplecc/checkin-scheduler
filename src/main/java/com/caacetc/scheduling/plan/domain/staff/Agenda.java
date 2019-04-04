package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.OpenFragment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
                .sorted()
                .filter(workDay -> workDay.date().isBefore(task.date()))
                .findFirst();
        if (before.isPresent()) {
            if (before.get().offDuty().until(task.startTime(), HOURS) < 12) {
                return false;
            }
        }
        Optional<WorkDay> after = workDays.stream()
                .sorted()
                .filter(workDay -> workDay.date().isAfter(task.date()))
                .findFirst();
        if (after.isPresent()) {
            if (after.get().onDuty().until(task.endTime(), HOURS) < 12) {
                return false;
            }
        }
        // mostly continue 4 days todo
        workDays.stream()
                .map(workDay -> workDay.date().until(task.date()).getDays());

        return true;
    }

    public int workDaysNum() {
        return workDays.size();
    }

    public List<WorkDay> workplans() {
        return workDays;
    }
}

package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.OpenFragment;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工工作类，包含上班时间、下班时间、工作在柜台的时间片段
 */
public class WorkDay {
    private final LocalDate date;
    private final LocalDateTime onDuty;
    private final LocalDateTime offDuty;
    private final List<OpenFragment> tasks;

    public WorkDay(LocalDateTime onDuty) {
        this.date = onDuty.toLocalDate();
        this.onDuty = onDuty;
        this.offDuty = onDuty.plusHours(8);
        this.tasks = new ArrayList<>();
    }

    // todo: add test
    public LocalDateTime mondayThisWeek() {
        int days = date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        return date.minusDays(days).atStartOfDay();
    }

    public List<OpenFragment> tasks() {
        return tasks.stream().sorted().collect(Collectors.toList());
    }

    public void add(OpenFragment task) {
        tasks.add(task);
    }

    public LocalDate date() {
        return date;
    }

    public LocalDateTime offDuty() {
        return offDuty;
    }

    public LocalDateTime onDuty() {
        return onDuty;
    }
}

package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.OpenFragment;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工工作类，包含上班时间、下班时间、工作在柜台的时间片段
 */
public class WorkDay {
    private final LocalDateTime onDuty;
    private final LocalDateTime offDuty;
    private final List<OpenFragment> tasks;

    public WorkDay(LocalDateTime onDuty) {
        this.onDuty = onDuty;
        this.offDuty = onDuty.plusHours(8);
        this.tasks = new ArrayList<>();
    }

    public LocalDateTime mondayThisWeek() {
        int days = onDuty.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        return onDuty.minusDays(days);
    }

    public List<OpenFragment> workPeriods() {
        return tasks.stream().sorted().collect(Collectors.toList());
    }

    public void add(OpenFragment workPeriod) {
        tasks.add(workPeriod);
    }

    public LocalDateTime offDuty() {
        return offDuty;
    }

    public LocalDateTime onDuty() {
        return onDuty;
    }
}

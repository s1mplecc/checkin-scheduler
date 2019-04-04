package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.OpenPeriod;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工工作类，包含上班时间、下班时间、工作在柜台的时间片段
 */
public class WorkDuration {
    @Getter
    private final LocalDateTime onDuty;
    @Getter
    private final LocalDateTime offDuty;
    private final List<OpenPeriod> workPeriods;

    public WorkDuration(LocalDateTime onDuty) {
        this.onDuty = onDuty;
        this.offDuty = onDuty.plusHours(8);
        this.workPeriods = new ArrayList<>();
    }

    public LocalDateTime mondayThisWeek() {
        int days = onDuty.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        return onDuty.minusDays(days);
    }

    public List<OpenPeriod> workPeriods() {
        return workPeriods.stream().sorted().collect(Collectors.toList());
    }

    public void add(OpenPeriod workPeriod) {
        workPeriods.add(workPeriod);
    }

    public LocalDateTime offDuty() {
        return offDuty;
    }

    public LocalDateTime onDuty() {
        return onDuty;
    }
}

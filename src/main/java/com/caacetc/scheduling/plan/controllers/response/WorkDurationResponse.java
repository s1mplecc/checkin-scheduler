package com.caacetc.scheduling.plan.controllers.response;

import com.caacetc.scheduling.plan.domain.counter.OpenFragment;
import com.caacetc.scheduling.plan.domain.staff.WorkDay;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
class WorkDurationResponse {
    private String day;
    private String startTime;
    private String endTime;
    private List<OpenFragment> workPeriods;

    WorkDurationResponse(WorkDay workDay) {
        this.day = workDay.onDuty().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.startTime = workDay.onDuty().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.endTime = workDay.offDuty().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.workPeriods = workDay.workPeriods();
    }
}

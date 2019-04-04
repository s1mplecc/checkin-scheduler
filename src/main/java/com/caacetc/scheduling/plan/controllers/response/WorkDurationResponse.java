package com.caacetc.scheduling.plan.controllers.response;

import com.caacetc.scheduling.plan.domain.counter.OpenFragment;
import com.caacetc.scheduling.plan.domain.staff.WorkDuration;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
class WorkDurationResponse {
    private String day;
    private String startTime;
    private String endTime;
    private List<OpenFragment> workPeriods;

    WorkDurationResponse(WorkDuration workDuration) {
        this.day = workDuration.onDuty().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.startTime = workDuration.onDuty().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.endTime = workDuration.offDuty().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.workPeriods = workDuration.workPeriods();
    }
}

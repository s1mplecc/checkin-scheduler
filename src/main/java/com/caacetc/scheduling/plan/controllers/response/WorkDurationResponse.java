package com.caacetc.scheduling.plan.controllers.response;

import com.caacetc.scheduling.plan.domain.counter.OpenPeriod;
import com.caacetc.scheduling.plan.domain.staff.WorkDuration;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.List;

@Data
class WorkDurationResponse {
    private String day;
    private String startTime;
    private String endTime;
    private List<OpenPeriod> workPeriods;

    WorkDurationResponse(WorkDuration workDuration) {
        this.day = new SimpleDateFormat("yyyy-MM-dd").format(workDuration.onDuty().getTime());
        this.startTime = new SimpleDateFormat("HH:mm").format(workDuration.onDuty().getTime());
        this.endTime = new SimpleDateFormat("HH:mm").format(workDuration.offDuty().getTime());
        this.workPeriods = workDuration.getWorkPeriods();
    }
}

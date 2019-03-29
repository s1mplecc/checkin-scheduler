package com.caacetc.scheduling.plan.controllers.response;

import com.caacetc.scheduling.plan.domain.counter.OpenPeriod;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
class OpenPeriodResponse {
    private String day;
    private String startTime;
    private String endTime;

    OpenPeriodResponse(OpenPeriod openPeriod) {
        this.day = new SimpleDateFormat("yyyy-MM-dd").format(openPeriod.startTime().getTime());
        this.startTime = new SimpleDateFormat("HH:mm").format(openPeriod.startTime().getTime());
        this.endTime = new SimpleDateFormat("HH:mm").format(openPeriod.endTime().getTime());
    }
}

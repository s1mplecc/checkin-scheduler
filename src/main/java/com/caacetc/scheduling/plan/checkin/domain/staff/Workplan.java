package com.caacetc.scheduling.plan.checkin.domain.staff;

import java.util.Calendar;

public class Workplan {
    private Calendar startTime;
    private Calendar endTime;

    public Workplan(Calendar startTime, Calendar endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

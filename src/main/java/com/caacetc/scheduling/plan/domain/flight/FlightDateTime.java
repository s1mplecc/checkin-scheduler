package com.caacetc.scheduling.plan.domain.flight;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class FlightDateTime {
    private String day;
    private String domEndTime;
    private String intEndTime;

    public FlightDateTime(String day) {
        this.day = day;
        this.domEndTime = "23:59:59";
        this.intEndTime = "23:59:59";
    }

    public String day() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String domEndTime() {
        return domEndTime;
    }

    public void setDomEndTime(LocalDateTime domEndTime) {
        this.domEndTime = new SimpleDateFormat("HH:mm:ss").format(domEndTime);
    }

    public String intEndTime() {
        return intEndTime;
    }

    public void setIntEndTime(LocalDateTime intEndTime) {
        this.intEndTime = new SimpleDateFormat("HH:mm:ss").format(intEndTime);
    }
}

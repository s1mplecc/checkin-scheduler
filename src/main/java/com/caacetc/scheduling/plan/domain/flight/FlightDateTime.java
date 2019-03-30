package com.caacetc.scheduling.plan.domain.flight;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightDateTime {
    private String day;
    private String domEndTime;
    private String intEndTime;

    public FlightDateTime(String day) {
        this.day = day;
        this.domEndTime = "23:59";
        this.intEndTime = "23:59";
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

    public void setDomEndTime(Date domEndTime) {
        this.domEndTime = new SimpleDateFormat("HH:mm").format(domEndTime);
    }

    public String intEndTime() {
        return intEndTime;
    }

    public void setIntEndTime(Date intEndTime) {
        this.intEndTime = new SimpleDateFormat("HH:mm").format(intEndTime);
    }
}

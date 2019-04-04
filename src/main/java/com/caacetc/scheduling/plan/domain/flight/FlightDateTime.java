package com.caacetc.scheduling.plan.domain.flight;

import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
public class FlightDateTime {
    private LocalDate date;
    private LocalTime domEndTime;
    private LocalTime intEndTime;

    public FlightDateTime(LocalDate date) {
        this.date = date;
    }

    public LocalDate date() {
        return date;
    }

    public LocalTime domEndTime() {
        return domEndTime;
    }

    public LocalTime intEndTime() {
        return intEndTime;
    }
}

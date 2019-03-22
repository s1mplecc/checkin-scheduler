package com.caacetc.scheduling.plan.checkin.domain.staff;

public class ScheduleStaffException extends RuntimeException {
    public ScheduleStaffException() {
        super();
    }

    public ScheduleStaffException(String message) {
        super(message);
    }

    public ScheduleStaffException(String message, Throwable cause) {
        super(message, cause);
    }
}

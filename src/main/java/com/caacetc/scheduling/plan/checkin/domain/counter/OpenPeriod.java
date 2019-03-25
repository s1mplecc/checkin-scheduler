package com.caacetc.scheduling.plan.checkin.domain.counter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OpenPeriod {
    private Calendar startTime;
    private Calendar endTime;
    private String staffId;

    public OpenPeriod(Calendar startTime, Calendar endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isGt3Hours() {
        long l = (endTime.getTime().getTime() - startTime.getTime().getTime());
        return l > 3 * (1000 * 60 * 60);
    }

    public Calendar startTime() {
        return startTime;
    }

    public Calendar endTime() {
        return endTime;
    }

    public List<OpenPeriod> split() {
        List<OpenPeriod> result = new ArrayList<>();

        OpenPeriod current = this;
        Calendar tempStartTime = startTime;
        while (current.endTime().before(endTime)) {
            result.add(current);

            Calendar after3Hours = (Calendar) tempStartTime.clone();
            after3Hours.add(Calendar.HOUR, 3);
            current = new OpenPeriod(tempStartTime, after3Hours);
            tempStartTime = after3Hours;
        }
        result.add(new OpenPeriod(tempStartTime, endTime));

        return result;
    }
}

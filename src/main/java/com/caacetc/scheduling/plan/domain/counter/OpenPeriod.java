package com.caacetc.scheduling.plan.domain.counter;

import com.caacetc.scheduling.plan.domain.staff.Staff;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Data
public class OpenPeriod implements Comparable<OpenPeriod> {
    private Calendar startTime;
    private Calendar endTime;
    private String staffName;

    public OpenPeriod(Calendar startTime, Calendar endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void assign(List<Staff> staffs) {
        Staff one = staffs.stream()
                .filter(staff -> staff.isLegal(this))
                .sorted()
                .findFirst()
                .orElse(Staff.nobody());

        this.staffName = one.name();
        one.addWorkPlan(this);
    }

    public int durationHours() {
        long l = (endTime.getTime().getTime() - startTime.getTime().getTime());
        return (int) (l / (1000 * 60 * 60));
    }

    public boolean gt3Hours() {
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

        Calendar tempStartTime = startTime;
        Calendar after3Hours = (Calendar) tempStartTime.clone();
        after3Hours.add(Calendar.HOUR_OF_DAY, 3);

        while (after3Hours.before(endTime)) {
            result.add(new OpenPeriod((Calendar) tempStartTime.clone(), (Calendar) after3Hours.clone()));
            tempStartTime.add(Calendar.HOUR_OF_DAY, 3);
            after3Hours.add(Calendar.HOUR_OF_DAY, 3);
        }

        result.add(new OpenPeriod(tempStartTime, endTime));

        return result;
    }

    public void append(Calendar endTime) {
        this.endTime = endTime;
    }

    public void propel(Calendar startTime) {
        this.startTime = startTime;
    }

    public boolean longerThan1Hour() {
        long l = endTime.getTime().getTime() - startTime.getTime().getTime();
        return l >= 1000 * 60 * 60;
    }

    public boolean only15minutesAfter(OpenPeriod lastOne) {
        long l = lastOne.endTime().getTime().getTime() - this.startTime.getTime().getTime();
        return l <= 1000 * 60 * 15;
    }

    public void combineWith(OpenPeriod openPeriod) {
        this.endTime = openPeriod.endTime;
    }

    @Override
    public int compareTo(OpenPeriod another) {
        return (int) (this.startTime.getTime().getTime() - another.startTime().getTime().getTime());
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("MM-dd HH:mm").format(startTime.getTime())
                + " ~ " +
                new SimpleDateFormat("MM-dd HH:mm").format(endTime.getTime());
    }
}

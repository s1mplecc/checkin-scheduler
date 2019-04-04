package com.caacetc.scheduling.plan.domain.counter;

import com.caacetc.scheduling.plan.domain.staff.Staff;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;

@Data
public class OpenPeriod implements Comparable<OpenPeriod> {
    private final String counterCode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String staffName;

    public OpenPeriod(String counterCode, LocalDateTime startTime, LocalDateTime endTime) {
        this.counterCode = counterCode;
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

    public boolean gte3Hours() {
        return startTime.until(endTime, HOURS) >= 3;
    }

    public LocalDateTime startTime() {
        return startTime;
    }

    public LocalDateTime endTime() {
        return endTime;
    }

    public void append(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void propel(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public int compareTo(OpenPeriod another) {
        return startTime.compareTo(another.startTime);
    }
}

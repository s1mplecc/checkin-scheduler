package com.caacetc.scheduling.plan.domain.counter;

import com.caacetc.scheduling.plan.domain.staff.Staff;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;

/**
 * As open fragment to Counter view,
 * as task to Staff view.
 */
@Data
public class OpenFragment implements Comparable<OpenFragment> {
    private final String counterCode;
    private String staffName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    public OpenFragment(String counterCode, LocalDateTime startTime, LocalDateTime endTime) {
        this.counterCode = counterCode;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void assign(List<Staff> staffs) {
        Staff one = staffs.stream()
                .filter(staff -> staff.isLegal(this))
                .min(Comparator.comparingInt(o -> o.agenda().workDaysNum()))
                .orElse(Staff.nobody());

        staffName = one.name();
        one.assignTask(this);
    }

    public boolean gte3Hours() {
        return startTime.until(endTime, HOURS) >= 3;
    }

    public List<OpenFragment> splitBy3Hours() {
        if (!gte3Hours()) {
            return Collections.singletonList(this);
        } else {
            List<OpenFragment> openFragments = new ArrayList<>();
            LocalDateTime flag = startTime;
            while (!flag.isAfter(endTime)) {
                openFragments.add(new OpenFragment(counterCode, flag, flag.plusHours(3)));
                flag = flag.plusHours(3);
            }
            return openFragments;
        }
    }

    public LocalDateTime startTime() {
        return startTime;
    }

    public LocalDateTime endTime() {
        return endTime;
    }

    public int durationHours() {
        return (int) startTime.until(endTime, HOURS);
    }

    public LocalDate date() {
        return startTime().toLocalDate();
    }

    @Override
    public int compareTo(OpenFragment another) {
        return startTime.compareTo(another.startTime);
    }
}

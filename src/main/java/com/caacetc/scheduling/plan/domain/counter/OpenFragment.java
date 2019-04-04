package com.caacetc.scheduling.plan.domain.counter;

import com.caacetc.scheduling.plan.domain.staff.Staff;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;

@Data
public class OpenFragment implements Comparable<OpenFragment> {
    private final String counterCode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String staffName;

    public OpenFragment(String counterCode, LocalDateTime startTime, LocalDateTime endTime) {
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

    @Override
    public int compareTo(OpenFragment another) {
        return startTime.compareTo(another.startTime);
    }
}

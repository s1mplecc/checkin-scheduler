package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.Counter;
import com.caacetc.scheduling.plan.domain.counter.OpenPeriod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffScheduler {
    public List<Staff> scheduleBy(List<Counter> counters, List<Staff> staffs) {
        List<Staff> econCheckInStaffs = filterByJob(staffs, "经济舱值机");
        List<Staff> premCheckInStaffs = filterByJob(staffs, "高端值机");

        counters.forEach(counter -> {
            List<OpenPeriod> openPeriods = counter.openPeriodsAfterSplit();
            if (counter.isPrem()) {
                openPeriods.forEach(openPeriod -> openPeriod.assign(premCheckInStaffs));
            } else {
                openPeriods.forEach(openPeriod -> openPeriod.assign(econCheckInStaffs));
            }
        });

        List<Staff> staffs2 = new ArrayList<>();
        staffs2.addAll(econCheckInStaffs);
        staffs2.addAll(premCheckInStaffs);
        return staffs2;
    }

    private List<Staff> filterByJob(List<Staff> staffs, String job) {
        return staffs.stream()
                .filter(staff -> staff.job().equals(job))
                .collect(Collectors.toList());
    }
}

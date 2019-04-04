package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.Counter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffScheduler {
    public List<Staff> scheduleBy(List<Counter> counters, List<Staff> inStaffs) {
        List<Staff> staffs = new ArrayList<>();
        List<Staff> economyCheckInStaffs = filterByJob(inStaffs, "经济舱值机");
        List<Staff> premiumCheckInStaffs = filterByJob(inStaffs, "高端值机");
        staffs.addAll(economyCheckInStaffs);
        staffs.addAll(premiumCheckInStaffs);

        counters.stream()
                .filter(Counter::isPremium)
                .forEach(counter -> counter.openPeriods().forEach(openPeriod -> openPeriod.assign(premiumCheckInStaffs)));
        counters.stream()
                .filter(Counter::isEconomy)
                .forEach(counter -> counter.openPeriods().forEach(openPeriod -> openPeriod.assign(economyCheckInStaffs)));
        return staffs;
    }

    private List<Staff> filterByJob(List<Staff> staffs, String job) {
        return staffs.stream()
                .filter(staff -> staff.job().equals(job))
                .collect(Collectors.toList());
    }
}

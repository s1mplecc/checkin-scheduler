package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.Counter;
import com.caacetc.scheduling.plan.domain.counter.OpenFragment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffScheduler {
    public List<Staff> scheduleBy(List<Counter> counters, List<Staff> staffs) {
        List<Staff> economyCheckInStaffs = filterByJob(staffs, "经济舱值机");
        List<Staff> premiumCheckInStaffs = filterByJob(staffs, "高端值机");

        counters.forEach(counter -> {
            List<OpenFragment> openFragments = counter.openPeriods();
            if (counter.isPremium()) {
                openFragments.forEach(openPeriod -> openPeriod.assign(premiumCheckInStaffs));
            } else {
                openFragments.forEach(openPeriod -> openPeriod.assign(economyCheckInStaffs));
            }
        });

        List<Staff> staffs2 = new ArrayList<>();
        staffs2.addAll(economyCheckInStaffs);
        staffs2.addAll(premiumCheckInStaffs);
        return staffs2;
    }

    private List<Staff> filterByJob(List<Staff> staffs, String job) {
        return staffs.stream()
                .filter(staff -> staff.job().equals(job))
                .collect(Collectors.toList());
    }
}

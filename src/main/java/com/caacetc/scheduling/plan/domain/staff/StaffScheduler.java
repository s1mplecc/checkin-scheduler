package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.counter.Counter;
import com.caacetc.scheduling.plan.domain.counter.OpenPeriod;
import com.caacetc.scheduling.plan.mapper.StaffMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaffScheduler {
    private List<Staff> econCheckInStaffs;
    private List<Staff> premCheckInStaffs;

    public StaffScheduler() {
        econCheckInStaffs = new StaffMapper().econCheckInStaffs();
        premCheckInStaffs = new StaffMapper().premCheckInStaffs();
    }

    public List<Staff> schedule(List<Counter> counters) {
        counters.forEach(counter -> {
            List<OpenPeriod> openPeriods = counter.openPeriodsAfterSplit();
            if (counter.isPrem()) {
                openPeriods.forEach(openPeriod -> openPeriod.assign(premCheckInStaffs));
            } else {
                openPeriods.forEach(openPeriod -> openPeriod.assign(econCheckInStaffs));
            }
        });

        List<Staff> staffs = new ArrayList<>();
        staffs.addAll(econCheckInStaffs);
        staffs.addAll(premCheckInStaffs);
        return staffs;
    }
}

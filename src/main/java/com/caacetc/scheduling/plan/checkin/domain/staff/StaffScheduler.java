package com.caacetc.scheduling.plan.checkin.domain.staff;

import com.caacetc.scheduling.plan.checkin.domain.counter.Counter;
import com.caacetc.scheduling.plan.checkin.domain.counter.OpenPeriod;
import com.caacetc.scheduling.plan.checkin.mapper.StaffMapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StaffScheduler {
    private LinkedList<Staff> econCheckInStaffs;
    private LinkedList<Staff> premCheckInStaffs;

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

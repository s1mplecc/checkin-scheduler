package com.caacetc.scheduling.plan.checkin.domain.staff;

import com.caacetc.scheduling.plan.checkin.domain.counter.Counter;
import com.caacetc.scheduling.plan.checkin.mapper.StaffMapper;

import java.util.List;

public class StaffScheduler {
    private List<Staff> econCheckInStaffs;
    private List<Staff> premCheckInStaffs;

    public StaffScheduler() {
        econCheckInStaffs = new StaffMapper().econCheckInStaffs();
        premCheckInStaffs = new StaffMapper().premCheckInStaffs();
    }

    public List<Staff> schedule(List<Counter> counters) {

        return null;
    }
}

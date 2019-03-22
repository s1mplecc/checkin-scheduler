package com.caacetc.scheduling.plan.checkin;

import com.caacetc.scheduling.plan.checkin.domain.Flight;
import com.caacetc.scheduling.plan.checkin.domain.Interval;
import com.caacetc.scheduling.plan.checkin.domain.PassengerDistribution;
import com.caacetc.scheduling.plan.checkin.domain.staff.Staff;
import com.caacetc.scheduling.plan.checkin.mapper.FlightMapper;

import java.util.List;

public class Schedule {
    private List<Staff> econStaffs;
    private List<Staff> premStaffs;

    public void schedule() {
        List<Flight> flights = new FlightMapper().flights();
        List<Interval> distribution = new PassengerDistribution().estimate(flights);
        List<Staff> staffs = schedule(distribution);
    }

    private List<Staff> schedule(List<Interval> distribution) {
        return null;
    }
}

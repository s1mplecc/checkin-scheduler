package com.caacetc.scheduling.plan.checkin.domain.staff;

import com.caacetc.scheduling.plan.checkin.domain.Flight;
import com.caacetc.scheduling.plan.checkin.domain.Interval;
import com.caacetc.scheduling.plan.checkin.domain.PassengerDistribution;
import com.caacetc.scheduling.plan.checkin.domain.counter.Counter;
import com.caacetc.scheduling.plan.checkin.domain.counter.CounterScheduler;
import com.caacetc.scheduling.plan.checkin.mapper.FlightMapper;
import org.junit.Test;

import java.util.List;

public class StaffSchedulerTest {
    @Test
    public void should_() {
        List<Flight> flights = new FlightMapper().flights();
        List<Interval> intervals = new PassengerDistribution().estimate(flights);
        List<Counter> counters = new CounterScheduler().schedule(intervals);
        List<Staff> staffs = new StaffScheduler().schedule(counters);
        for (Staff staff : staffs) {
            System.out.println(staff);
        }
    }
}
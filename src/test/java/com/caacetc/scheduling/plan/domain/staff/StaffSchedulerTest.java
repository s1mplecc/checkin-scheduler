package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.domain.Flight;
import com.caacetc.scheduling.plan.domain.Interval;
import com.caacetc.scheduling.plan.domain.PassengerDistribution;
import com.caacetc.scheduling.plan.domain.counter.Counter;
import com.caacetc.scheduling.plan.domain.counter.CounterScheduler;
import com.caacetc.scheduling.plan.mapper.FlightMapper;
import org.junit.Test;

import java.util.List;

public class StaffSchedulerTest {
    @Test
    public void should_schedule_staffs() {
        List<Flight> flights = new FlightMapper().flights();
        List<Interval> intervals = new PassengerDistribution().estimate(flights);
        List<Counter> counters = new CounterScheduler().schedule(intervals);
        List<Staff> staffs = new StaffScheduler().schedule(counters);
        for (Staff staff : staffs) {
            System.out.println(staff);
        }
    }
}
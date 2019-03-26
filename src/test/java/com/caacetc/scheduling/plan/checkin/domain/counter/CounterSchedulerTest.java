package com.caacetc.scheduling.plan.checkin.domain.counter;

import com.caacetc.scheduling.plan.checkin.domain.Flight;
import com.caacetc.scheduling.plan.checkin.domain.Interval;
import com.caacetc.scheduling.plan.checkin.domain.PassengerDistribution;
import com.caacetc.scheduling.plan.checkin.mapper.FlightMapper;
import org.junit.Test;

import java.util.List;

public class CounterSchedulerTest {
    @Test
    public void should_schedule_counter() {
        List<Flight> flights = new FlightMapper().flights();
        List<Interval> intervals = new PassengerDistribution().estimate(flights);
        List<Counter> counters = new CounterScheduler().schedule(intervals);
        for (Counter counter : counters) {
            System.out.println(counter);
        }
    }
}
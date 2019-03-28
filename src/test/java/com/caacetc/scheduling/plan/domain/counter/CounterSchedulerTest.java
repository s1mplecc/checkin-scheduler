package com.caacetc.scheduling.plan.domain.counter;

import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.domain.flight.Interval;
import com.caacetc.scheduling.plan.domain.flight.PassengerDistribution;
import com.caacetc.scheduling.plan.gateway.FlightMapper;
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
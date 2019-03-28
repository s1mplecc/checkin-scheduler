package com.caacetc.scheduling.plan.checkin;

import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.domain.flight.Interval;
import com.caacetc.scheduling.plan.domain.flight.PassengerDistribution;
import com.caacetc.scheduling.plan.gateway.FlightMapper;
import org.junit.Test;

import java.util.List;

public class PassengerDistributionTest {
    @Test
    public void should_() {
        List<Flight> flights = new FlightMapper().flights();
        List<Interval> estimate = new PassengerDistribution().estimate(flights);
        for (Interval interval : estimate) {
            System.out.println(interval);
        }
    }
}
package com.caacetc.scheduling.plan.checkin;

import org.junit.Test;

import java.util.List;

public class PassengerDistributionTest {
    @Test
    public void should_() {
        List<Flight> flights = new FlightMapper().flights();
        System.out.println(flights.get(0));

        List<PassengerDistribution.Interval> estimate = new PassengerDistribution().estimate(flights);

        for (PassengerDistribution.Interval interval : estimate) {
            System.out.println(interval);
        }
    }
}
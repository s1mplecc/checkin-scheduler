package com.caacetc.scheduling.plan.checkin;

import org.junit.Test;

import java.util.List;

public class FlightTest {
    @Test
    public void should_() {
        List<Flight> flights = new FlightMapper().flights();
        System.out.println(flights.size());
        System.out.println(flights.get(0));
        System.out.println(flights.get(1000));
    }
}
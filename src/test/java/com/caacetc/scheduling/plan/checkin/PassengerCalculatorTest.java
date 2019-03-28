package com.caacetc.scheduling.plan.checkin;

import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.domain.flight.PassengerCalculator;
import com.caacetc.scheduling.plan.domain.flight.PassengerDistribution;
import com.caacetc.scheduling.plan.gateway.FlightMapper;
import org.junit.Test;

import java.util.List;

public class PassengerCalculatorTest {
    @Test
    public void should_() {
        List<Flight> flights = new FlightMapper().flights();
        List<PassengerDistribution> estimate = new PassengerCalculator().estimate(flights);
        for (PassengerDistribution passengerDistribution : estimate) {
            System.out.println(passengerDistribution);
        }
    }
}
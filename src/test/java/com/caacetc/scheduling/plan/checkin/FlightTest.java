package com.caacetc.scheduling.plan.checkin;

import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.gateway.FlightMapper;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FlightTest {
    @Test
    public void should_load_flights_from_db_and_compute_cabin_counts() {
        List<Flight> flights = new FlightMapper().flights();

        assertThat(flights.size()).isEqualTo(2650);

        assertThat(flights.get(0).date()).isEqualTo("2019/1/14");
        assertThat(flights.get(0).passengerNum()).isEqualTo("#PEK-ZHY[P:1,0,0][Y:34,0,0]#PEK-URC[Y:118,0,0]");
        assertThat(flights.get(0).economyCabinNum()).isEqualTo(152);
        assertThat(flights.get(0).premiumCabinNum()).isEqualTo(1);

        assertThat(flights.get(2649).passengerNum()).isEqualTo("#PEK-NNG[F:2,0,0][Y:150,0,0]");
        assertThat(flights.get(2649).economyCabinNum()).isEqualTo(150);
        assertThat(flights.get(2649).premiumCabinNum()).isEqualTo(2);
    }
}
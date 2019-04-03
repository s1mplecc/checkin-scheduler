package com.caacetc.scheduling.plan.domain.flight;

import com.caacetc.scheduling.plan.controllers.request.FlightRequest;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public class FlightBuilder {
    public static Optional<Flight> with(FlightRequest flightRequest) {
        try {
            Flight flight = new Flight();
            flight.setRegion(flightRequest.getRegion());
            flight.setEconomyCabinNum(flightRequest.getEconomyCabinNum());
            flight.setPremiumCabinNum(flightRequest.getPremiumCabinNum());

            try {
                TemporalAccessor parse = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(flightRequest.getDepartTime());
                flight.setDepartTime(Instant.from(parse));
            } catch (DateTimeParseException ex) {
                TemporalAccessor parse = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(flightRequest.getDepartTime());
                flight.setDepartTime(Instant.from(parse));
            }

            return Optional.of(flight);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}

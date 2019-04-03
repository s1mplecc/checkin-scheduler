package com.caacetc.scheduling.plan.controllers.request;

import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.domain.flight.FlightBuilder;
import com.caacetc.scheduling.plan.domain.staff.Staff;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@Data
public class ScheduleRequest {
    private List<FlightRequest> flights;
    private List<StaffRequest> staffs;
    private List<RuleRequest> rules;

    public List<RuleRequest> rules() {
        return rules;
    }

    /**
     * @return sorted flights by departTime
     */
    public List<Flight> sortedFlights() {
        if (flights == null || flights.isEmpty()) {
            throw new InvalidRequestParamException("sortedFlights is null or empty.");
        }

        return flights.stream()
                .map(FlightBuilder::with)
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Staff> staffs() {
        if (staffs == null || staffs.isEmpty()) {
            throw new InvalidRequestParamException("staffs is null or empty.");
        }

        return staffs.stream().map(Staff::new).collect(Collectors.toList());
    }
}

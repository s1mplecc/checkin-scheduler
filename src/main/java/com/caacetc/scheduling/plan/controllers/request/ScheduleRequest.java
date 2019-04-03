package com.caacetc.scheduling.plan.controllers.request;

import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.domain.staff.Staff;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class ScheduleRequest {
    private List<FlightRequest> flights;
    private List<StaffRequest> staffs;
    private List<RuleRequest> rules;

    public List<RuleRequest> rules() {
        return rules;
    }

    public List<Flight> flights() {
        if (flights == null || flights.isEmpty()) {
            throw new InvalidRequestParamException("flights is null or empty.");
        }

        return flights.stream()
                .filter(f -> f.getDepartTime() != null && !f.getDepartTime().isEmpty())
                .map(Flight::new)
                .collect(Collectors.toList());
    }

    public List<Staff> staffs() {
        if (staffs == null || staffs.isEmpty()) {
            throw new InvalidRequestParamException("staffs is null or empty.");
        }

        return staffs.stream().map(Staff::new).collect(Collectors.toList());
    }
}

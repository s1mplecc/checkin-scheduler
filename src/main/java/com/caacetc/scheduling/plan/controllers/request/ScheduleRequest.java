package com.caacetc.scheduling.plan.controllers.request;

import com.caacetc.scheduling.plan.domain.flight.Flight;
import com.caacetc.scheduling.plan.domain.staff.Staff;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@ToString
public class ScheduleRequest {
    private List<FlightRequest> flights;
    private List<StaffRequest> staffs;
    private List<RuleRequest> rules;

    public List<RuleRequest> rules() {
        return rules;
    }

    public List<Flight> flights() {
        return flights.stream().map(Flight::new).collect(Collectors.toList());
    }

    public List<Staff> staffs() {
        return staffs.stream().map(Staff::new).collect(Collectors.toList());
    }
}

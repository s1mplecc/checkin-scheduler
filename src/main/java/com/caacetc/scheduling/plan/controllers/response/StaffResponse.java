package com.caacetc.scheduling.plan.controllers.response;

import com.caacetc.scheduling.plan.domain.staff.Staff;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class StaffResponse {
    private String name;
    private List<WorkDurationResponse> workPlans;

    public StaffResponse(Staff staff) {
        name = staff.name();
        workPlans = staff.agenda().workplans().stream().map(WorkDurationResponse::new).collect(Collectors.toList());
    }
}

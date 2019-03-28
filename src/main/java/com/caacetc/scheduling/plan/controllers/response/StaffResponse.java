package com.caacetc.scheduling.plan.controllers.response;

import com.caacetc.scheduling.plan.domain.staff.Staff;
import com.caacetc.scheduling.plan.domain.staff.WorkDuration;
import lombok.Data;

import java.util.List;

@Data
public class StaffResponse {
    private String id;
    private String type;
    private List<WorkDuration> workPlans;

    public StaffResponse(Staff staff) {
        id = staff.name();
        type = staff.job();
        workPlans = staff.agenda().workplans();
    }
}

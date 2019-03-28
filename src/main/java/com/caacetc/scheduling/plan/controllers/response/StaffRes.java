package com.caacetc.scheduling.plan.controllers.response;

import com.caacetc.scheduling.plan.domain.staff.Staff;
import com.caacetc.scheduling.plan.domain.staff.WorkDuration;
import lombok.Data;

import java.util.List;

@Data
public class StaffRes {
    private String id;
    private String type;
    private List<WorkDuration> workPlans;

    public StaffRes(Staff staff) {
        id = staff.id();
        type = staff.type();
        workPlans = staff.agenda().workplans();
    }
}

package com.caacetc.scheduling.plan.checkin.domain.staff;

import lombok.ToString;

@ToString
public class Staff {
    private String id;
    private String type;
    private Agenda agenda;

    public Staff(String id, String type) {
        this.id = id;
        this.type = type;
        this.agenda = new Agenda();
    }

    public String id() {
        return id;
    }

    public void addWorkPlan(Workplan workplan) {
        if (!isLegal(workplan)) {
            throw new ScheduleStaffException();
        }
        agenda.add(workplan);
    }

    public boolean isLegal(Workplan workplan) {
        return true;
    }

    public void dailyLaborHours() {

    }

    public void monthLaborHours() {

    }
}

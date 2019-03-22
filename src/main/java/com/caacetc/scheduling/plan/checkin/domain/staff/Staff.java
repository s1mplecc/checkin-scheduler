package com.caacetc.scheduling.plan.checkin.domain.staff;

public class Staff {
    private String id;
    private Agenda agenda;

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

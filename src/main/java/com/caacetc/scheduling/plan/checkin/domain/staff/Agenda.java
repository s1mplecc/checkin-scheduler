package com.caacetc.scheduling.plan.checkin.domain.staff;

import lombok.ToString;

import java.util.List;

@ToString
public class Agenda {
    private List<Workplan> workplans;

    public void add(Workplan workPlan) {

    }

    public boolean oneWeekLte5Days() {
        return false;
    }

    public boolean mostlyContinue4Days() {
        return false;
    }

    public boolean lastIntervalGt12Hours() {
        return false;
    }
}

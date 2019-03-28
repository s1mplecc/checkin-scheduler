package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.controllers.request.StaffReq;
import com.caacetc.scheduling.plan.domain.counter.OpenPeriod;
import lombok.ToString;

@ToString
public class Staff implements Comparable<Staff> {
    private String id;
    private String name;
    private String type;
    private Agenda agenda;


    public Staff(StaffReq staffReq) {
    }

    public Staff(String id, String type) {
        this.id = id;
        this.type = type;
        this.agenda = new Agenda();
    }

    public String id() {
        return id;
    }

    /**
     * 1、每个员工上班即上 8hours
     * 2、一周上 5 天班
     * 3、最多连续 4 天
     * 4、两次上班间隔 > 12hours
     */
    public void addWorkPlan(OpenPeriod openPeriod) {
        agenda.add(openPeriod);
    }

    public boolean isLegal(OpenPeriod openPeriod) {
        return agenda.oneWeekLte5Days(openPeriod)
                && agenda.mostlyContinue4Days(openPeriod)
                && agenda.lastIntervalGt12Hours(openPeriod)
                && agenda.inWorkDuration(openPeriod);
    }

    public Agenda agenda() {
        return agenda;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Staff another) {
        return agenda.workHours() - another.agenda().workHours();
    }

    public String type() {
        return type;
    }
}

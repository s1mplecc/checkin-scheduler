package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.controllers.request.StaffRequest;
import com.caacetc.scheduling.plan.domain.counter.OpenPeriod;
import lombok.ToString;

@ToString
public class Staff implements Comparable<Staff> {
    private String name;
    private String job;
    private Agenda agenda;

    public Staff(StaffRequest staffRequest) {
        this.name = staffRequest.getName();
        this.job = staffRequest.getJob();
    }

    public Staff(String name, String job) {
        this.name = name;
        this.job = job;
        this.agenda = new Agenda();
    }

    public String id() {
        return name;
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
        return job;
    }
}

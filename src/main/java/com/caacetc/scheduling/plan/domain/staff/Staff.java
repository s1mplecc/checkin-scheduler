package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.controllers.request.StaffRequest;
import com.caacetc.scheduling.plan.domain.counter.OpenPeriod;
import lombok.ToString;

@ToString
public class Staff implements Comparable<Staff> {
    private static final Staff NOBODY = new Staff();
    private String name;
    private String job;
    private Agenda agenda;

    public Staff(StaffRequest staffRequest) {
        this.name = staffRequest.getName();
        this.job = staffRequest.getJob();
        this.agenda = new Agenda();
    }

    private Staff() {
    }

    public static Staff nobody() {
        return NOBODY;
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
        boolean b = agenda.oneWeekLte5Days(openPeriod)
                && agenda.mostlyContinue4Days(openPeriod)
                && agenda.lastIntervalGt12Hours(openPeriod)
                && agenda.inWorkDuration(openPeriod);
        return b;
    }

    public String name() {
        return name;
    }

    public String job() {
        return job;
    }

    public Agenda agenda() {
        return agenda;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Staff another) {
        return agenda.workHours() - another.agenda().workHours();
    }
}

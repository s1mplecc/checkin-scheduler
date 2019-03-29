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
        this.name = "NOBODY";
        this.agenda = new Agenda();
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

    // todo-zz: bug fix
    public boolean isLegal(OpenPeriod openPeriod) {
        return agenda.oneWeekLte5Days(openPeriod)
                && agenda.mostlyContinue4Days(openPeriod)
                && agenda.lastIntervalGt12Hours(openPeriod)
                && agenda.inWorkDuration(openPeriod);
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

    @Override
    public int compareTo(Staff another) {
        return agenda.workHours() - another.agenda().workHours();
    }
}

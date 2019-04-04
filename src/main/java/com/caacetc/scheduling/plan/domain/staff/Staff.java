package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.controllers.request.StaffRequest;
import com.caacetc.scheduling.plan.domain.counter.OpenFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Staff implements Comparable<Staff> {
    private static final Logger log = LoggerFactory.getLogger(Staff.class);

    private static final Staff NOBODY = new Staff();

    private final String name;
    private final Job job;
    private final Agenda agenda;

    public Staff(StaffRequest staffRequest) {
        this.name = staffRequest.getName();
        this.job = Job.of(staffRequest.getJob());
        this.agenda = new Agenda();
    }

    private Staff() {
        this.name = "NOBODY";
        this.job = Job.INVALID;
        this.agenda = new Agenda();
    }

    public static Staff nobody() {
        log.warn("Staff nobody was assigned");
        return NOBODY;
    }

    /**
     * 1、每个员工上班即上 8hours
     * 2、一周上 5 天班
     * 3、最多连续 4 天
     * 4、两次上班间隔 > 12hours
     */
    public void assignTask(OpenFragment task) {
        agenda.add(task);
    }

    public boolean isLegal(OpenFragment task) {
        return agenda.isLegal(task);
    }

    public String name() {
        return name;
    }

    public Job job() {
        return job;
    }

    public Agenda agenda() {
        return agenda;
    }

    @Override
    public int compareTo(Staff another) {
        return agenda.workDaysNum() - another.agenda.workDaysNum();
    }
}

package com.caacetc.scheduling.plan.checkin.domain.staff;

import lombok.ToString;

@ToString
public class Staff implements Comparable<Staff> {
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

    /**
     * 1、每个员工上班即上 8hours
     * 2、一周上 5 天班
     * 3、最多连续 4 天
     * 4、两次上班间隔 > 12hours
     */
    public void addWorkPlan(WorkDuration workDuration) {
        agenda.add(workDuration);
    }

    public boolean isLegal(WorkDuration workDuration) {
        return agenda.oneWeekLte5Days(workDuration)
                && agenda.mostlyContinue4Days(workDuration)
                && agenda.lastIntervalGt12Hours(workDuration);
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

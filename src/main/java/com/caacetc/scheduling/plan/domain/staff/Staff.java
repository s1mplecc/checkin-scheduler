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
        return true;
//        AtomicReference<WorkDuration> workDuration0 = new AtomicReference<>();
//        boolean existWorkPlan = agenda.workplans().stream()
//                .anyMatch(workDuration -> {
//                    if (workDuration.onDuty().get(Calendar.DATE) == openPeriod.startTime().get(Calendar.DATE)) {
//                        workDuration0.set(workDuration);
//                        return true;
//                    }
//                    return false;
//                });
//
//        if (existWorkPlan) {
//            boolean periodStartTimeAfterOnDuty = !openPeriod.startTime().before(workDuration0.get().onDuty());
//            boolean periodEndTimeBeforeOffDuty = !openPeriod.endTime().after(workDuration0.get().offDuty());
//
//            boolean couldInsertPeriod = workDuration0.get().workPeriods().stream()
//                    .sorted()
//                    .filter(openPeriod1 -> openPeriod1.startTime().after(openPeriod.startTime()))
//                    .findFirst()
//                    .filter(openPeriod1 -> openPeriod.endTime().before(openPeriod1.startTime()))
//                    .isPresent();
//
//            return periodStartTimeAfterOnDuty && periodEndTimeBeforeOffDuty && couldInsertPeriod;
//        } else {
//
//            boolean oneWeekLte5Days = agenda.oneWeekLte5Days(openPeriod);
//            boolean mostlyContinue4Days = agenda.mostlyContinue4Days(openPeriod);
//            boolean lastIntervalGt12Hours = agenda.lastIntervalGt12Hours(openPeriod);
//            boolean inWorkDuration = agenda.inWorkDuration(openPeriod);
//
//            return oneWeekLte5Days && mostlyContinue4Days && lastIntervalGt12Hours && inWorkDuration;
//        }
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

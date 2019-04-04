package com.caacetc.scheduling.plan.domain.staff;

import com.caacetc.scheduling.plan.controllers.request.StaffRequest;
import com.caacetc.scheduling.plan.domain.counter.OpenFragment;

public class Staff implements Comparable<Staff> {
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
        return NOBODY;
    }

    /**
     * 1、每个员工上班即上 8hours
     * 2、一周上 5 天班
     * 3、最多连续 4 天
     * 4、两次上班间隔 > 12hours
     */
    public void assignTask(OpenFragment openFragment) {
        agenda.add(openFragment);
    }

    // todo-zz: bug fix
    public boolean isLegal(OpenFragment openFragment) {
        return true;
//        AtomicReference<WorkDay> workDuration0 = new AtomicReference<>();
//        boolean existWorkPlan = agenda.workplans().stream()
//                .anyMatch(workDuration -> {
//                    if (workDuration.onDuty().get(Calendar.DATE) == openFragment.startTime().get(Calendar.DATE)) {
//                        workDuration0.set(workDuration);
//                        return true;
//                    }
//                    return false;
//                });
//
//        if (existWorkPlan) {
//            boolean periodStartTimeAfterOnDuty = !openFragment.startTime().before(workDuration0.get().onDuty());
//            boolean periodEndTimeBeforeOffDuty = !openFragment.endTime().after(workDuration0.get().offDuty());
//
//            boolean couldInsertPeriod = workDuration0.get().tasks().stream()
//                    .sorted()
//                    .filter(openPeriod1 -> openPeriod1.startTime().after(openFragment.startTime()))
//                    .findFirst()
//                    .filter(openPeriod1 -> openFragment.endTime().before(openPeriod1.startTime()))
//                    .isPresent();
//
//            return periodStartTimeAfterOnDuty && periodEndTimeBeforeOffDuty && couldInsertPeriod;
//        } else {
//
//            boolean oneWeekLte5Days = agenda.oneWeekLte5Days(openFragment);
//            boolean mostlyContinue4Days = agenda.mostlyContinue4Days(openFragment);
//            boolean lastIntervalGt12Hours = agenda.lastIntervalGt12Hours(openFragment);
//            boolean inWorkDuration = agenda.inWorkDuration(openFragment);
//
//            return oneWeekLte5Days && mostlyContinue4Days && lastIntervalGt12Hours && inWorkDuration;
//        }
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

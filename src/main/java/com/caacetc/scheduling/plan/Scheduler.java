package com.caacetc.scheduling.plan;

import java.util.*;
import java.util.stream.Collectors;

public class Scheduler {
    private static final int CYCLE = 2;
    private final List<DailyPlan> dailyPlans;
    private final float morningRate;
    private final float afternoonRate;
    private final float nightRate;
    private LinkedList<Staff> staffs;

    public Scheduler(List<DailyPlan> dailyPlans) {
        this.dailyPlans = dailyPlans;
        this.morningRate = morningRate();
        this.afternoonRate = afternoonRate();
        this.nightRate = nightRate();
    }

    public List<Staff> staffs() {
        return staffs.stream().sorted().collect(Collectors.toList());
    }

    public void schedule() {
        int expectStaffsNum = expectStaffsNum();
        assignStaffs(expectStaffsNum);
    }

    public void alter(int staffId, int date) {
        Staff staff = staffs().get(staffId);

        Period alterPeriod = staff.workPlans().stream()
                .filter(workPlan -> workPlan.date() == date)
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .period();

        Set<Staff> possibleAlterStaffs;
        // todo: date + 1
        switch (alterPeriod) {
            case MORNING:
                possibleAlterStaffs = dailyPlans.get(date + 1).morning().assignedStaffs();
                break;
            case AFTERNOON:
                possibleAlterStaffs = dailyPlans.get(date + 1).afternoon().assignedStaffs();
                break;
            case NIGHT:
                possibleAlterStaffs = dailyPlans.get(date + 1).night().assignedStaffs();
                break;
            default:
                possibleAlterStaffs = new HashSet<>();
        }
        boolean isAlter = false;
        while (!isAlter) {
            for (Staff possibleAlterStaff : possibleAlterStaffs) {
                // todo: date + 1
                if (!breakRestRule(date, possibleAlterStaff) && !breakRestRule(date + 1, staff)) {
                    System.out.println("Date_" + (date + 1) + " " + staffId + " ---> " + possibleAlterStaff.id());
                    isAlter = true;
                    break;
                }
            }
        }

    }

    private boolean breakRestRule(int date, Staff possibleAlterStaff) {
        return possibleAlterStaff.workPlans().stream()
                .mapToInt(Staff.WorkPlan::date)
                .anyMatch(date0 -> Math.abs(date - date0) <= 1);
    }

    private void assignStaffs(int expectStaffsNum) {
        staffs = initStaffList(expectStaffsNum);
        try {
            for (DailyPlan dailyPlan : dailyPlans) {
                dailyPlan.assign(staffs, morningRate, afternoonRate, nightRate);
            }
        } catch (StaffNotEnoughException e) {
            for (DailyPlan dailyPlan : dailyPlans) {
                dailyPlan.clear();
            }
            assignStaffs(expectStaffsNum + 1);
        }
    }

    public float morningRate() {
        float total = dailyPlans.stream().mapToInt(DailyPlan::totalNumber).sum();
        float morning = dailyPlans.stream().mapToInt(dailyPlan -> dailyPlan.morning().number()).sum();
        return morning / total;
    }

    public float afternoonRate() {
        float total = dailyPlans.stream().mapToInt(DailyPlan::totalNumber).sum();
        float afternoon = dailyPlans.stream().mapToInt(dailyPlan -> dailyPlan.afternoon().number()).sum();
        return afternoon / total;
    }

    public float nightRate() {
        float total = dailyPlans.stream().mapToInt(DailyPlan::totalNumber).sum();
        float night = dailyPlans.stream().mapToInt(dailyPlan -> dailyPlan.night().number()).sum();
        return night / total;
    }

    public int expectStaffsNum() {
        List<Integer> needs = new ArrayList<>();
        for (DailyPlan dailyPlan : dailyPlans) {
            needs.add(dailyPlan.morning().number());
            needs.add(dailyPlan.afternoon().number());
            needs.add(dailyPlan.night().number());
        }
        int max = 0;
        for (int i = 0; i <= needs.size() - 3 * CYCLE; i++) {
            int temp = 0;
            for (int j = i; j < i + 3 * CYCLE; j++) {
                temp += needs.get(j);
            }
            if (temp > max) {
                max = temp;
            }
        }
        return max;
    }

    private LinkedList<Staff> initStaffList(int totalStaffNum) {
        staffs = new LinkedList<>();
        for (int i = 0; i < totalStaffNum; i++) {
            staffs.add(new Staff(i));
        }
        return staffs;
    }
}

package com.caacetc.scheduling.plan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Scheduler {
    private static final int CYCLE = 2;
    private final List<DailyPlan> dailyPlans;
    private LinkedList<Staff> staffs;

    public Scheduler(List<DailyPlan> dailyPlans) {
        this.dailyPlans = dailyPlans;
    }

    public List<Staff> staffs() {
        return staffs.stream().sorted().collect(Collectors.toList());
    }

    public void schedule() {
        int expectStaffsNum = expectStaffsNum();
        assignStaffs(expectStaffsNum);
    }

    private void assignStaffs(int expectStaffsNum) {
        staffs = initStaffList(expectStaffsNum);
        try {
            for (DailyPlan dailyPlan : dailyPlans) {
                dailyPlan.assign(staffs);
            }
        } catch (StaffNotEnoughException e) {
            for (DailyPlan dailyPlan : dailyPlans) {
                dailyPlan.clear();
            }
            assignStaffs(expectStaffsNum + 1);
        }
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

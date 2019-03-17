package com.caacetc.scheduling.plan;

import java.util.LinkedList;
import java.util.List;

public class Scheduling {
    private final List<DailyPlan> dailyPlans;
    private List<Staff> staffList;

    public Scheduling(List<DailyPlan> dailyPlans) {
        this.dailyPlans = dailyPlans;
    }

    public int expectStaffs() {
        int max = 0;
        for (int i = 0; i < dailyPlans.size() - 2; i++) {
            int temp = dailyPlans.get(i).totalNumber() + dailyPlans.get(i + 1).totalNumber() + dailyPlans.get(i + 2).totalNumber();
            if (temp > max) {
                max = temp;
            }
        }
        return max;
    }

    public void schedule() {
        int totalStaffNum = expectStaffs();
        staffList = initStaffList(totalStaffNum);

        for (DailyPlan dailyPlan : dailyPlans) {

        }
    }

    private List<Staff> initStaffList(int totalStaffNum) {
        staffList = new LinkedList<>();
        for (int i = 0; i < totalStaffNum; i++) {
            staffList.add(new Staff(i));
        }
        return staffList;
    }
}

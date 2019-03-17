package com.caacetc.scheduling.plan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Scheduling {
    private static final int CYCLE = 3;
    private final List<DailyPlan> dailyPlans;
    private LinkedList<Staff> staffs;

    public Scheduling(List<DailyPlan> dailyPlans) {
        this.dailyPlans = dailyPlans;
    }

    public void schedule() {
        int expectStaffs = expectStaffs();
        staffs = initStaffList(expectStaffs);

        for (DailyPlan dailyPlan : dailyPlans) {
            dailyPlan.morning().assign(staffs);
            dailyPlan.afternoon().assign(staffs);
            dailyPlan.night().assign(staffs);
        }
    }

    public int expectStaffs() {
        List<Integer> needs = new ArrayList<>();
        for (DailyPlan dailyPlan : dailyPlans) {
            needs.add(dailyPlan.morningNumber());
            needs.add(dailyPlan.afternoonNumber());
            needs.add(dailyPlan.nightNumber());
        }
        int max = 0;
        int begin = 0;
        for (int i = 0; i <= needs.size() - 3 * CYCLE; i++) {
            int temp = 0;
            for (int j = i; j < i + 3 * CYCLE; j++) {
                temp += needs.get(j);
            }
            if (temp > max) {
                max = temp;
                begin = i;
            }
        }
        System.out.println(String.format("至少需要%d名员工，忙碌峰值从：%s到%s", max, dateFormatBy(begin), dateFormatBy(begin + CYCLE * 3 - 1)));
        return max;
    }

    private String dateFormatBy(int index) {
        return String.format("第%d天%s",
                1 + index / 3, index % 3 == 0 ? "早上" : index % 3 == 1 ? "中午" : "晚上");
    }

    private LinkedList<Staff> initStaffList(int totalStaffNum) {
        staffs = new LinkedList<>();
        for (int i = 0; i < totalStaffNum; i++) {
            staffs.add(new Staff(i));
        }
        return staffs;
    }
}

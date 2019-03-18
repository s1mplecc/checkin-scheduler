package com.caacetc.scheduling.plan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.caacetc.scheduling.plan.Period.*;

public class Staff implements Comparable<Staff> {
    private final int id;
    private final List<WorkPlan> workPlans;
    private final Set<Period> periodFlags;
    private int lastDate;

    public Staff(int id) {
        this.id = id;
        this.workPlans = new ArrayList<>();
        this.periodFlags = new HashSet<>();
    }

    public boolean isHaveRest(int currentDate) {
        return currentDate - lastDate > 1;
    }

    public boolean isBalancedAfterAssign(Period period) {
        return !periodFlags.contains(period);
    }

    public void addWorkPlan(int date, Period period) {
        if (!isBalancedAfterAssign(period)) {
            throw new RuntimeException();
        }

        workPlans.add(new WorkPlan(date, period));
        periodFlags.add(period);
        if (periodFlags.size() == 3) {
            periodFlags.clear();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("STAFF_").append(id).append(": { ");
        for (WorkPlan workPlan : workPlans) {
            sb.append(workPlan).append(", ");
        }
        sb.deleteCharAt(sb.length() - 2).append("} | { ");
        int total = workPlans.size();
        long morning = workPlans.stream().filter(workPlan -> MORNING.equals(workPlan.period)).count();
        long afternoon = workPlans.stream().filter(workPlan -> AFTERNOON.equals(workPlan.period)).count();
        long night = workPlans.stream().filter(workPlan -> NIGHT.equals(workPlan.period)).count();
        sb.append("总计(天): ").append(total).append(", 早班(天): ").append(morning)
                .append(", 中班(天)").append(afternoon).append(", 晚班(天): ").append(night).append(" }");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((Staff) obj).id;
    }

    @Override
    public int compareTo(Staff another) {
        return this.id - another.id;
    }

    static class WorkPlan {
        private final int date;
        private final Period period;

        WorkPlan(int date, Period period) {
            this.date = date;
            this.period = period;
        }

        @Override
        public String toString() {
            return String.format("%d日%s", date, period);
        }
    }
}

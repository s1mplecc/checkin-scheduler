package com.caacetc.scheduling.plan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Staff {
    private final int id;
    private final List<WorkPlan> workPlans;
    private final Set<String> flags;

    public Staff(int id) {
        this.id = id;
        this.workPlans = new ArrayList<>();
        this.flags = new HashSet<>();
    }

    public boolean isBalancedAfterAssign(String period) {
        return !flags.contains(period);
    }

    public void addWorkPlan(int date, String period) {
        if (!isBalancedAfterAssign(period)) {
            throw new RuntimeException("安排早中晚不均衡");
        }

        workPlans.add(new WorkPlan(date, period));
        flags.add(period);
        if (flags.size() == 3) {
            flags.clear();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("STAFF_").append(id).append(": { ");
        for (WorkPlan workPlan : workPlans) {
            sb.append(workPlan).append(", ");
        }
        sb.append("}.");
        int total = workPlans.size();
        long morning = workPlans.stream().filter(workPlan -> "早".equals(workPlan.period)).count();
        long afternoon = workPlans.stream().filter(workPlan -> "中".equals(workPlan.period)).count();
        long night = workPlans.stream().filter(workPlan -> "晚".equals(workPlan.period)).count();
        sb.append("总计上班").append(total).append("天，其中早班").append(morning)
                .append("，中班").append(afternoon).append("，晚班").append(night);
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

    static class WorkPlan {
        private final int date;
        private final String period;

        WorkPlan(int date, String period) {
            this.date = date;
            this.period = period;
        }

        @Override
        public String toString() {
            return String.format("%d日%s", date, period);
        }
    }
}

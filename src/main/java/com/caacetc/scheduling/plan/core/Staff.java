package com.caacetc.scheduling.plan.core;

import java.util.ArrayList;
import java.util.List;

import static com.caacetc.scheduling.plan.core.Period.*;

public class Staff implements Comparable<Staff> {
    private final int id;
    private final List<WorkPlan> workPlans;
    private int lastDate = -999;

    public Staff(int id) {
        this.id = id;
        this.workPlans = new ArrayList<>();
    }

    public List<WorkPlan> workPlans() {
        return workPlans;
    }

    public boolean nextIsExpectedPeriod(Period period, float morningRate, float afternoonRate, float nightRate) {
        if (workPlans.size() == 0) {
            return true;
        } else {
            return period.equals(expectNext(morningRate, afternoonRate, nightRate));
        }
    }

    public Period expectNext(float morningRate, float afternoonRate, float nightRate) {
        float total = workPlans.size();
        float morning = workPlans.stream().filter(workPlan -> MORNING.equals(workPlan.period())).count();
        float afternoon = workPlans.stream().filter(workPlan -> AFTERNOON.equals(workPlan.period())).count();
        float night = workPlans.stream().filter(workPlan -> NIGHT.equals(workPlan.period())).count();

        float m0 = Math.abs((morning + 1) / (total + 1) - morningRate);
        float a0 = Math.abs((afternoon + 1) / (total + 1) - afternoonRate);
        float n0 = Math.abs((night + 1) / (total + 1) - nightRate);

        float min = Math.min(Math.min(m0, a0), n0);
        return min == m0 ? MORNING : min == a0 ? AFTERNOON : NIGHT;
    }

    public boolean isHaveRest(int currentDate) {
        return currentDate - lastDate > 1;
    }

    public void addWorkPlan(int date, Period period) {
        lastDate = date;
        workPlans.add(new WorkPlan(date, period));
    }

    public float morningRate() {
        float total = workPlans.size();
        float morning = workPlans.stream().filter(workPlan -> MORNING.equals(workPlan.period())).count();
        return morning / total;
    }

    public float afternoonRate() {
        float total = workPlans.size();
        float afternoon = workPlans.stream().filter(workPlan -> AFTERNOON.equals(workPlan.period())).count();
        return afternoon / total;
    }

    public float nightRate() {
        float total = workPlans.size();
        float night = workPlans.stream().filter(workPlan -> NIGHT.equals(workPlan.period())).count();
        return night / total;
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
                .append(", 中班(天)").append(afternoon).append(", 晚班(天): ").append(night).append(" }")
                .append(" | { 早班占比: ").append(morningRate())
                .append(", 中班占比: ").append(afternoonRate())
                .append(", 晚班占比: ").append(nightRate()).append(" }");
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

    public int id() {
        return id;
    }

    static class WorkPlan {
        private final int date;
        private final Period period;

        WorkPlan(int date, Period period) {
            this.date = date;
            this.period = period;
        }

        public Period period() {
            return period;
        }

        public int date() {
            return date;
        }

        @Override
        public String toString() {
            return String.format("%d日%s", date, period);
        }
    }
}

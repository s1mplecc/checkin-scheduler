package com.caacetc.scheduling.plan.core;

import java.util.ArrayList;
import java.util.List;

public class DailyPlansGenerator {
    private List<DailyPlan> dailyPlans;
    private int lowerBound;
    private int upperBound;

    public DailyPlansGenerator(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public List<DailyPlan> generate(int days) {
        dailyPlans = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            dailyPlans.add(new DailyPlan(i + 1, lowerBound, upperBound));
        }
        return dailyPlans;
    }

    @Override
    public String toString() {
        if (dailyPlans.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (DailyPlan dailyPlan : dailyPlans) {
            sb.append(dailyPlan.toString()).append("\n");
        }
        return sb.toString();
    }
}

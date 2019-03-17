package com.caacetc.scheduling.plan;

import org.junit.Test;

import java.util.List;

public class DailyPlanGeneratorTest {
    @Test
    public void should_() {
        DailyPlansGenerator generator = new DailyPlansGenerator(2, 10);
        List<DailyPlan> dailyPlans = generator.generate(31);
        Scheduling scheduling = new Scheduling(dailyPlans);
        System.out.println(generator);
        System.out.println(scheduling.expectStaffs());
    }
}
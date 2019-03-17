package com.caacetc.scheduling.plan;

import org.junit.Test;

import java.util.List;

public class DailyPlanGeneratorTest {
    @Test
    public void should_schedule_staff_correctly() {
        DailyPlansGenerator generator = new DailyPlansGenerator(2, 10);
        List<DailyPlan> dailyPlans = generator.generate(31);
        System.out.println(generator);

        Scheduling scheduling = new Scheduling(dailyPlans);
        scheduling.schedule();
        for (Staff staff : scheduling.staffs()) {
            System.out.println(staff);
        }
    }
}
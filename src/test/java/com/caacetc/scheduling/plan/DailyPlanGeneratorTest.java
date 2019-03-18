package com.caacetc.scheduling.plan;

import org.junit.Test;

import java.util.List;

public class DailyPlanGeneratorTest {
    @Test
    public void should_schedule_staff_correctly() {
        DailyPlansGenerator generator = new DailyPlansGenerator(1, 10);
        List<DailyPlan> dailyPlans = generator.generate(31);
        System.out.println(generator);

        Scheduler scheduler = new Scheduler(dailyPlans);
        scheduler.schedule();
        for (Staff staff : scheduler.staffs()) {
            System.out.println(staff);
        }
        System.out.println(scheduler.morningRate());
        System.out.println(scheduler.afternoonRate());
        System.out.println(scheduler.nightRate());
    }
}
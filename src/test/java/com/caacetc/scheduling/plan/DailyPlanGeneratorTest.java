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

        System.out.println("早班总占比: " + scheduler.morningRate());
        System.out.println("中班总占比: " + scheduler.afternoonRate());
        System.out.println("晚班总占比: " + scheduler.nightRate() + "\n");

        for (Staff staff : scheduler.staffs()) {
            System.out.println(staff);
        }

        System.out.println();
        scheduler.alter(0, 1);
    }
}
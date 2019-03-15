package com.caacetc.scheduling.plan;

import org.junit.Test;

public class DailyPlanGeneratorTest {
    @Test
    public void should_() {
        DailyPlansGenerator generator = new DailyPlansGenerator(2, 10);
        generator.generate(31);
        System.out.println(generator);
    }
}
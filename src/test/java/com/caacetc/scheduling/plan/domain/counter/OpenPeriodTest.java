package com.caacetc.scheduling.plan.domain.counter;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenPeriodTest {
    @Test
    public void should_judge_if_gte3Hours() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime before = now.minusHours(2);
        assertThat(new OpenPeriod("A", before, now).gte3Hours()).isFalse();

        LocalDateTime before2 = now.minusHours(3);
        assertThat(new OpenPeriod("A", before2, now).gte3Hours()).isTrue();

        LocalDateTime before3 = now.minusHours(4);
        assertThat(new OpenPeriod("A", before3, now).gte3Hours()).isTrue();
    }
}
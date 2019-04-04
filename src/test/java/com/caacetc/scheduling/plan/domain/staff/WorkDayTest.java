package com.caacetc.scheduling.plan.domain.staff;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkDayTest {
    @Test
    public void should_return_monday_this_week() {
        LocalDateTime parse = LocalDateTime.parse("2019-01-14T10:15:30");
        LocalDateTime localDateTime = new WorkDay(parse).mondayThisWeek();
        assertThat(localDateTime.toLocalDate().getDayOfMonth()).isEqualTo(14);

        LocalDateTime parse0 = LocalDateTime.parse("2019-01-17T10:15:30");
        LocalDateTime localDateTime0 = new WorkDay(parse0).mondayThisWeek();
        assertThat(localDateTime0.toLocalDate().getDayOfMonth()).isEqualTo(14);

        LocalDateTime parse1 = LocalDateTime.parse("2019-01-13T10:15:30");
        LocalDateTime localDateTime1 = new WorkDay(parse1).mondayThisWeek();
        assertThat(localDateTime1.toLocalDate().getDayOfMonth()).isEqualTo(7);
    }
}
package com.caacetc.scheduling.plan.checkin.domain.staff;

import org.junit.Test;

import java.util.Calendar;

import static java.util.Calendar.*;
import static org.assertj.core.api.Assertions.assertThat;

public class WorkplanTest {
    @Test
    public void should_return_monday_this_week() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.MARCH, 1, 1, 1, 1);

        Workplan workplan = new Workplan(calendar, calendar);
        Calendar mondayThisWeek = workplan.mondayThisWeek();

        assertThat(mondayThisWeek.get(Calendar.DAY_OF_WEEK)).isEqualTo(MONDAY);
        assertThat(mondayThisWeek.get(Calendar.MONTH)).isEqualTo(FEBRUARY);
        assertThat(mondayThisWeek.get(Calendar.DAY_OF_MONTH)).isEqualTo(28);

        calendar.add(Calendar.DAY_OF_WEEK, 3);
        Workplan workplan2 = new Workplan(calendar, calendar);
        Calendar mondayThisWeek2 = workplan2.mondayThisWeek();

        assertThat(mondayThisWeek.equals(mondayThisWeek2)).isTrue();


        calendar.add(Calendar.DAY_OF_WEEK, 4);
        Workplan workplan3 = new Workplan(calendar, calendar);
        Calendar mondayThisWeek3 = workplan3.mondayThisWeek();

        assertThat(mondayThisWeek3.get(Calendar.DAY_OF_WEEK)).isEqualTo(MONDAY);
        assertThat(mondayThisWeek3.get(Calendar.MONTH)).isEqualTo(MARCH);
        assertThat(mondayThisWeek3.get(Calendar.DAY_OF_MONTH)).isEqualTo(6);
    }

    @Test
    public void should_2019_1_20_monday_is_1_14() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, JANUARY, 14, 0, 0, 0);

        Workplan workplan = new Workplan(calendar, calendar);
        Calendar mondayThisWeek = workplan.mondayThisWeek();

        assertThat(mondayThisWeek.get(Calendar.DAY_OF_WEEK)).isEqualTo(MONDAY);
        assertThat(mondayThisWeek.get(Calendar.MONTH)).isEqualTo(JANUARY);
        assertThat(mondayThisWeek.get(Calendar.DAY_OF_MONTH)).isEqualTo(14);
    }
}
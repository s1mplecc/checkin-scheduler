package com.caacetc.scheduling.plan.checkin.domain.counter;

import org.junit.Test;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenPeriodTest {
    @Test
    public void should_is_greater_than_3_hours() {
        Calendar start = Calendar.getInstance();
        Calendar end = (Calendar) start.clone();
        end.add(Calendar.HOUR, 2);

        OpenPeriod openPeriod = new OpenPeriod(start, end);
        assertThat(openPeriod.isGt3Hours()).isFalse();

        end.add(Calendar.HOUR, 1);
        OpenPeriod openPeriod2 = new OpenPeriod(start, end);
        assertThat(openPeriod2.isGt3Hours()).isFalse();

        end.add(Calendar.MINUTE, 1);
        OpenPeriod openPeriod3 = new OpenPeriod(start, end);
        assertThat(openPeriod3.isGt3Hours()).isTrue();
    }
}
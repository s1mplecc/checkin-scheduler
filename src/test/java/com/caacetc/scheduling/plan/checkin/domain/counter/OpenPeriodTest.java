package com.caacetc.scheduling.plan.checkin.domain.counter;

import org.junit.Test;

import java.util.Calendar;
import java.util.List;

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

    @Test
    public void should_split_when_interval_greater_than_3_hours() {
        Calendar start = Calendar.getInstance();
        start.set(3010, Calendar.MARCH, 1, 0, 0, 0);
        Calendar end = (Calendar) start.clone();
        end.add(Calendar.HOUR, 7);
        end.add(Calendar.MINUTE, 30);

        List<OpenPeriod> split = new OpenPeriod(start, end).split();

        assertThat(split.size()).isEqualTo(3);
        assertThat(split.get(0).startTime().get(Calendar.HOUR)).isEqualTo(0);
        assertThat(split.get(0).endTime().get(Calendar.HOUR)).isEqualTo(3);
        assertThat(split.get(1).endTime().get(Calendar.HOUR)).isEqualTo(6);
        assertThat(split.get(1).startTime().get(Calendar.MONTH)).isEqualTo(Calendar.MARCH);
        assertThat(split.get(2).endTime().get(Calendar.HOUR)).isEqualTo(7);
        assertThat(split.get(2).endTime().get(Calendar.MINUTE)).isEqualTo(30);
    }
}
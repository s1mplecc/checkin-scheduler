package com.caacetc.scheduling.plan.checkin;

import com.caacetc.scheduling.plan.domain.counter.Counter;
import com.caacetc.scheduling.plan.mapper.CounterMapper;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CounterTest {
    @Test
    public void should_load_counters_from_db() {
        List<Counter> counters = new CounterMapper().counters();

        assertThat(counters.size()).isEqualTo(148);

        assertThat(counters.get(0).id()).isEqualTo("A01");
        assertThat(counters.get(0).region()).isEqualTo("D/I");
        assertThat(counters.get(0).type()).isEqualTo("高端");

        assertThat(counters.get(147).id()).isEqualTo("L10");
        assertThat(counters.get(147).region()).isEqualTo("D");
        assertThat(counters.get(147).type()).isEqualTo("经济");
    }
}
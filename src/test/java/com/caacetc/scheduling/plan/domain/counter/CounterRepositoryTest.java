package com.caacetc.scheduling.plan.domain.counter;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CounterRepositoryTest {

    private CounterRepository counterRepository;

    @Before
    public void setUp() {
        counterRepository = new CounterRepository();
    }

    @Test
    public void should_return_counters() {
        List<Counter> mustOpenCounters = counterRepository.mustOpenCounters();
        List<Counter> onDemandPreCounters = counterRepository.onDemandPremiumCounters();
        List<Counter> onDemandDomEcoCounters = counterRepository.onDemandDomEconomyCounters();
        List<Counter> onDemandIntEcoCounters = counterRepository.onDemandIntEconomyCounters();

        assertThat(mustOpenCounters.size()).isEqualTo(16);
        assertThat(onDemandPreCounters.size()).isEqualTo(20);
        assertThat(onDemandDomEcoCounters.size()).isEqualTo(24);
        assertThat(onDemandIntEcoCounters.size()).isEqualTo(5);
    }
}
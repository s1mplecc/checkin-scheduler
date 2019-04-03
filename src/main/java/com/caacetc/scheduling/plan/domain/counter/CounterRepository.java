package com.caacetc.scheduling.plan.domain.counter;

import com.caacetc.scheduling.plan.gateway.JooqGateway;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class CounterRepository {
    @Resource
    private JooqGateway gateway;

    public Stream<Counter> counters() {
        return gateway.counters().parallelStream().peek(Counter::clear);
    }

    public List<Counter> mustOpenCounters() {
        return counters()
                .filter(Counter::mustOpen)
                .collect(Collectors.toList());
    }

    public List<Counter> onDemandDomEconomyCounters() {
        return counters()
                .filter(counter -> !counter.mustOpen())
                .filter(counter -> {
                    boolean a = counter.code().compareTo("K06") >= 0 && counter.code().compareTo("K12") <= 0;
                    boolean b = counter.code().compareTo("K17") >= 0 && counter.code().compareTo("K26") <= 0;
                    boolean c = counter.code().compareTo("L01") >= 0 && counter.code().compareTo("L04") <= 0;
                    boolean d = counter.code().compareTo("L05") >= 0 && counter.code().compareTo("L07") <= 0;
                    return a || b || c || d;
                })
                .collect(Collectors.toList());
    }

    public List<Counter> onDemandIntEconomyCounters() {
        return counters()
                .filter(counter -> !counter.mustOpen())
                .filter(counter -> counter.code().compareTo("H04") >= 0 && counter.code().compareTo("H08") <= 0)
                .collect(Collectors.toList());
    }

    public List<Counter> onDemandPremiumCounters() {
        return counters()
                .filter(counter -> !counter.mustOpen())
                .filter(counter -> {
                    boolean a = counter.code().compareTo("F21") >= 0 && counter.code().compareTo("F31") <= 0;
                    boolean b = counter.code().compareTo("A09") >= 0 && counter.code().compareTo("A12") <= 0;
                    boolean c = counter.code().compareTo("A18") >= 0 && counter.code().compareTo("A22") <= 0;
                    return a || b || c;
                })
                .collect(Collectors.toList());
    }
}

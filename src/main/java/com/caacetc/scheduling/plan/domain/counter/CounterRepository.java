package com.caacetc.scheduling.plan.domain.counter;

import com.caacetc.scheduling.plan.gateway.MysqlGateway;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CounterRepository {
    private List<Counter> counters;

    public CounterRepository() {
        Result<Record> records = MysqlGateway.context()
                .select()
                .from("counter")
                .fetch();

        counters = records.stream()
                .reduce(new ArrayList<>(), (counters1, record) -> {
                    String id = (String) record.get("code");
                    String region = (String) record.get("国内/国际");
                    String type = (String) record.get("经济舱/高端");
                    int isMustOpen = (int) record.get("isMustOpen");
                    String openStartTime = (String) record.get("openStartTime");
                    String openEndTime = (String) record.get("openEndTime");
                    counters1.add(new Counter(id, region, type, isMustOpen, openStartTime, openEndTime));
                    return counters1;
                }, (l, a) -> l);
    }

    public List<Counter> counters() {
        return counters;
    }

    public List<Counter> mustOpenCounters() {
        return counters.stream()
                .filter(Counter::mustOpen)
                .collect(Collectors.toList());
    }

    public List<Counter> onDemandDomEconomyCounters() {
        return counters.stream()
                .filter(counter -> !counter.mustOpen())
                .filter(counter -> {
                    boolean a = counter.id().compareTo("K06") >= 0 && counter.id().compareTo("K12") <= 0;
                    boolean b = counter.id().compareTo("K17") >= 0 && counter.id().compareTo("K26") <= 0;
                    boolean c = counter.id().compareTo("L01") >= 0 && counter.id().compareTo("L04") <= 0;
                    boolean d = counter.id().compareTo("L05") >= 0 && counter.id().compareTo("L07") <= 0;
                    return a || b || c || d;
                })
                .collect(Collectors.toList());
    }

    public List<Counter> onDemandIntEconomyCounters() {
        return counters.stream()
                .filter(counter -> !counter.mustOpen())
                .filter(counter -> counter.id().compareTo("H04") >= 0 && counter.id().compareTo("H08") <= 0)
                .collect(Collectors.toList());
    }

    public List<Counter> onDemandPremiumCounters() {
        return counters.stream()
                .filter(counter -> !counter.mustOpen())
                .filter(counter -> {
                    boolean isF = counter.id().compareTo("F21") >= 0 && counter.id().compareTo("F31") <= 0;
                    boolean isA = counter.id().compareTo("A09") >= 0 && counter.id().compareTo("A12") <= 0;
                    boolean isA2 = counter.id().compareTo("A18") >= 0 && counter.id().compareTo("A22") <= 0;
                    return isF || isA || isA2;
                })
                .collect(Collectors.toList());
    }
}

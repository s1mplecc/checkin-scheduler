package com.caacetc.scheduling.plan.checkin.mapper;

import com.caacetc.scheduling.plan.checkin.domain.counter.Counter;
import com.caacetc.scheduling.plan.core.DBConnector;
import org.jooq.Record;
import org.jooq.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CounterMapper {
    public List<Counter> counters() {
        Result<Record> records = DBConnector.context()
                .select()
                .from("counter")
                .fetch();

        return records.stream()
                .reduce(new ArrayList<>(), (counters, record) -> {
                    String id = (String) record.get("柜台号");
                    String region = (String) record.get("国内/国际");
                    String type = (String) record.get("经济舱/高端");
                    counters.add(new Counter(id, region, type));
                    return counters;
                }, (l, a) -> l);
    }

    public List<Counter> premCounters() {
        return counters().stream()
                .filter(counter -> {
                    boolean isF = counter.id().compareTo("F21") >= 0 || counter.id().compareTo("F31") <= 0;
                    boolean isA = counter.id().compareTo("A09") >= 0 || counter.id().compareTo("A12") <= 0;
                    boolean isA2 = counter.id().compareTo("A18") >= 0 || counter.id().compareTo("A22") <= 0;
                    boolean isB02 = "B02".equals(counter.id());
                    return isF || isA || isA2 || isB02;
                })
                .collect(Collectors.toList());
    }
}

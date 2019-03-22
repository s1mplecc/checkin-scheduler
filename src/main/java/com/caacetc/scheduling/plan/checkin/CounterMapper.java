package com.caacetc.scheduling.plan.checkin;

import com.caacetc.scheduling.plan.core.DBConnector;
import org.jooq.Record;
import org.jooq.Result;

import java.util.ArrayList;
import java.util.List;

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
}

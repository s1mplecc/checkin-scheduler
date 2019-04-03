package com.caacetc.scheduling.plan.gateway;

import com.caacetc.scheduling.plan.domain.counter.Counter;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JooqGateway {
    @Resource
    private DSLContext context;

    public List<Counter> counters() {
        Result<Record> records = context.select().from("counter").fetch();

        return records.stream()
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
}

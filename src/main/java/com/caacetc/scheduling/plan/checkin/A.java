package com.caacetc.scheduling.plan.checkin;

import com.caacetc.scheduling.plan.core.DBConnector;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

public class A {
    public static void main(String[] args) {
        new A().a();
    }

    public void a() {
        DSLContext context = DBConnector.context();
        Result<Record> records = context.select()
                .from("flights")
                .fetch();
        System.out.println(records);
    }
}

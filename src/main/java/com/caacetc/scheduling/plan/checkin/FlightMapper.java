package com.caacetc.scheduling.plan.checkin;

import com.caacetc.scheduling.plan.core.DBConnector;
import org.jooq.Record;
import org.jooq.Result;

import java.util.ArrayList;
import java.util.List;

public class FlightMapper {
    public List<Flight> flights() {
        Result<Record> records = DBConnector.context()
                .select()
                .from("flights")
                .fetch();

        List<Flight> flights = new ArrayList<>();
        for (Record record : records) {
            String date = (String) record.get("航班日期");
            String departTime = (String) record.get("预计起飞时间");
            String passengerNum = (String) record.get("旅客人数");
            String region = (String) record.get("区域属性");
            flights.add(new Flight(date, departTime, passengerNum, region));
        }
        return flights;
    }
}

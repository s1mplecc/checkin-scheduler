package com.caacetc.scheduling.plan.checkin.mapper;

import com.caacetc.scheduling.plan.checkin.Flight;
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

        return records.stream()
                .reduce(new ArrayList<>(), (flights, record) -> {
                    String date = (String) record.get("航班日期");
                    String departTime = (String) record.get("预计起飞时间");
                    String passengerNum = (String) record.get("旅客人数");
                    String region = (String) record.get("区域属性");
                    String destination = (String) record.get("目的站");
                    flights.add(new Flight(date, departTime, passengerNum, region, destination));
                    return flights;
                }, (l, a) -> l);
    }
}

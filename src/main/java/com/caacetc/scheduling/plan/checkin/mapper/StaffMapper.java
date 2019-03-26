package com.caacetc.scheduling.plan.checkin.mapper;

import com.caacetc.scheduling.plan.checkin.domain.staff.Staff;
import com.caacetc.scheduling.plan.core.DBConnector;
import org.jooq.Record;
import org.jooq.Result;

import java.util.LinkedList;

public class StaffMapper {
    public LinkedList<Staff> econCheckInStaffs() {
        Result<Record> records = DBConnector.context()
                .select()
                .from("staff_econ_checkin")
                .fetch();

        return records.stream()
                .reduce(new LinkedList<>(), (staffs, record) -> {
                    String id = (String) record.get("员工编号");
                    String type = (String) record.get("岗位");
                    staffs.add(new Staff(id, type));
                    return staffs;
                }, (l, a) -> l);
    }

    public LinkedList<Staff> premCheckInStaffs() {
        Result<Record> records = DBConnector.context()
                .select()
                .from("staff_prem_checkin")
                .fetch();

        return records.stream()
                .reduce(new LinkedList<>(), (staffs, record) -> {
                    String id = (String) record.get("员工编号");
                    String type = (String) record.get("岗位");
                    staffs.add(new Staff(id, type));
                    return staffs;
                }, (l, a) -> l);
    }
}

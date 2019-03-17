package com.caacetc.scheduling.plan;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import static com.caacetc.scheduling.plan.Period.*;

public class DailyPlan {
    private int date;
    private Container morning;
    private Container afternoon;
    private Container night;

    public DailyPlan(int date, int lowerBound, int upperBound) {
        this.date = date;
        this.morning = new Container(MORNING, upperBound, lowerBound);
        this.afternoon = new Container(AFTERNOON, upperBound, lowerBound);
        this.night = new Container(NIGHT, upperBound, lowerBound);
    }

    public int totalNumber() {
        return morning.number() + afternoon.number() + night.number();
    }

    @Override
    public String toString() {
        return String.format("第 %s 天:  早上 %s 人  中午 %s 人  晚上 %s 人 ; 总计 %s 人",
                date, morning.number(), afternoon.number(), night.number(), totalNumber());
    }

    public Container morning() {
        return morning;
    }

    public Container afternoon() {
        return afternoon;
    }

    public Container night() {
        return night;
    }

    public void assign(LinkedList<Staff> staffs) {
        morning().assign(staffs);
        afternoon().assign(staffs);
        night().assign(staffs);
    }

    public class Container {
        private Period period;
        private int number;
        private Set<Staff> assignedStaffs;

        public Container(Period period, int upperBound, int lowerBound) {
            this.period = period;
            Random random = new Random();
            this.number = lowerBound + random.nextInt(upperBound - lowerBound);
            this.assignedStaffs = new HashSet<>();
        }

        public int number() {
            return number;
        }

        public void assign(LinkedList<Staff> staffs) {
            while (assignedStaffs.size() < number) {
                for (int i = 0; i < staffs.size(); i++) {
                    if (staffs.get(i).isBalancedAfterAssign(period)) {
                        Staff staff = staffs.remove(i);
                        assignedStaffs.add(staff);
                        staff.addWorkPlan(date, period);
                        staffs.addLast(staff);
                        break;
                    }
                }
            }
        }
    }
}

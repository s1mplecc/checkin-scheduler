package com.caacetc.scheduling.plan;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class DailyPlan {
    private int date;
    private Container morning;
    private Container afternoon;
    private Container night;

    public DailyPlan(int date, int lowerBound, int upperBound) {
        this.date = date;
        this.morning = new Container(lowerBound, upperBound);
        this.afternoon = new Container(lowerBound, upperBound);
        this.night = new Container(lowerBound, upperBound);
    }

    public int totalNumber() {
        return morning.number() + afternoon.number() + night.number();
    }

    public int morningNumber() {
        return morning.number();
    }

    public int afternoonNumber() {
        return afternoon.number();
    }

    public int nightNumber() {
        return night.number();
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

    public class Container {
        private int number;
        private Set<Staff> assignedStaffs;

        public Container(int lowerBound, int upperBound) {
            Random random = new Random();
            this.number = lowerBound + random.nextInt(upperBound - lowerBound);
            this.assignedStaffs = new HashSet<>();
        }

        public int number() {
            return number;
        }

        public void assign(LinkedList<Staff> staffs) {
            while (assignedStaffs.size() < number) {
                // todo: if morning, afternoon, night average
                Staff first = staffs.pollFirst();
                assignedStaffs.add(first);
                first.addWorkPlan(date, "早");
                staffs.addLast(first);
            }
        }
    }
}

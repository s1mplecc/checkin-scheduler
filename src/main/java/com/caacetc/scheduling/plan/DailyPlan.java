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

    private class Container {
        private int number;
        private Set<Staff> staffs;

        public Container(int lowerBound, int upperBound) {
            Random random = new Random();
            this.number = lowerBound + random.nextInt(upperBound - lowerBound);
            this.staffs = new HashSet<>();
        }

        public int number() {
            return number;
        }

        public void assign(LinkedList<Staff> staff) {
//            staff.pollFirst()
//            staffs.add(staff);
        }
    }
}

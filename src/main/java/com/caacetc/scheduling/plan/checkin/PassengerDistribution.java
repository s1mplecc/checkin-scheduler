package com.caacetc.scheduling.plan.checkin;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.List;

/**
 * 旅客分布区间，区间取 5min，从 00:00 ～ 24:00 的
 */
public class PassengerDistribution {
    public static void main(String[] args) {
        NormalDistribution distribution = new NormalDistribution(90, 10);
        System.out.println(distribution.getMean());
        System.out.println(distribution.probability(60, 120));
    }

    public void estimate(List<Flight> flights) {

    }
}

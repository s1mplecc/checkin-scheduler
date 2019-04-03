package com.caacetc.scheduling.plan.domain.flight;

import com.caacetc.scheduling.plan.controllers.request.FlightRequest;
import lombok.Data;
import lombok.ToString;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.caacetc.scheduling.plan.domain.flight.Region.DOM;
import static com.caacetc.scheduling.plan.domain.flight.Region.INT;

@ToString
@Data
public class Flight implements Comparable<Flight> {
    private long id;
    private Date departTime;
    private Region region;
    private int economyCabinNum;
    private int premiumCabinNum;

    public Flight(FlightRequest flightRequest) {
        this.id = flightRequest.getId();
        this.region = flightRequest.getRegion();
        this.economyCabinNum = flightRequest.getEconomyCabinNum();
        this.premiumCabinNum = flightRequest.getPremiumCabinNum();
        try {
            this.departTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(flightRequest.getDepartTime());
        } catch (ParseException ex) {
            try {
                this.departTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(flightRequest.getDepartTime());
            } catch (ParseException ex2) {
                throw new RuntimeException(ex2);
            }
        }
    }

    public Date departTime() {
        return departTime;
    }

    public int economyCabinNum() {
        return economyCabinNum;
    }

    public int premiumCabinNum() {
        return premiumCabinNum;
    }

    public boolean isDomestic() {
        return DOM.equals(region);
    }

    public boolean isInternational() {
        return INT.equals(region);
    }

    @Override
    public int compareTo(Flight o) {
        return (int) (this.departTime.getTime() - o.departTime().getTime());
    }
}

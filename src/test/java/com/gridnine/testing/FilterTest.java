package com.gridnine.testing;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class FilterTest {
    List<Flight> flights = new ArrayList<>();
    FlightFilterChain chain = new FlightFilterChain();
    @Test
    void departureBefore() {
        chain.addFilter(new DepartureBeforeNow());
        flights = FlightBuilder.createFlights();
        List<Flight> flights1 = chain.filter(flights);
        Assertions.assertEquals(5, flights1.size()); //один не подходит поэтому фильтр из 6 сделает 5
    }

    @Test
    void wrongSegemts() {
        chain.addFilter(new SegmentsWithDepartureBeforeArrival());
        flights = FlightBuilder.createFlights();
        List<Flight> flights1 = chain.filter(flights);
        Assertions.assertEquals(5, flights1.size());
    }

    @Test
    void onLandTime() {
        chain.addFilter(new SegmentsWithOnLandTimeMoreThan2());
        flights = FlightBuilder.createFlights();
        List<Flight> flights1 = chain.filter(flights);
        Assertions.assertEquals(4, flights1.size());
    }
    @Test
    void workTogether() {
        chain.addFilter(new SegmentsWithDepartureBeforeArrival());
        chain.addFilter(new SegmentsWithOnLandTimeMoreThan2());
        chain.addFilter(new DepartureBeforeNow());
        flights = FlightBuilder.createFlights();
        List<Flight> flights1 = chain.filter(flights);
        Assertions.assertEquals(2, flights1.size());

    }
}
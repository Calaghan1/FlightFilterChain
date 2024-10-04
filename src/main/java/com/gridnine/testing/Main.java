package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.time.format.DateTimeFormatter;
public class Main {
    public static void main(String[] args) {
        List<Flight> flight = FlightBuilder.createFlights();
        for (Flight f : flight) {
            System.out.println(f.toString());
        }
        System.out.println("-----------------------------");

        FlightFilterChain filterChain = new FlightFilterChain();
        filterChain.addFilter(new DepartureBeforeNow());
        List<Flight> flights = filterChain.filter(flight);
        for (Flight f : flights) {
            System.out.println(f);
        }
        System.out.println("-----------------------------");
        FlightFilterChain filterChain1 = new FlightFilterChain();
        filterChain1.addFilter(new SegmentsWithDepartureBeforeArrival());
        flight = FlightBuilder.createFlights();
        flights = filterChain1.filter(flight);
        for (Flight f : flights) {
            System.out.println(f);
        }
        System.out.println("-----------------------------");
        FlightFilterChain filterChain2 = new FlightFilterChain();
        filterChain2.addFilter(new SegmentsWithOnLandTimeMoreThan2());
        flight = FlightBuilder.createFlights();
        flights = filterChain2.filter(flight);
        for (Flight f : flights) {
            System.out.println(f);
        }
    }
}
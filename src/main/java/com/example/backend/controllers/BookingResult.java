package com.example.backend.controllers;

import java.util.List;

public class BookingResult {

    private Long flightId;
    private String departureCity;
    private String departureAirport;
    private String destinationCity;
    private String destinationAirport;
    private String departureTime;
    private String arrivalTime;
    private List<String> seatNumbers;
    private String message;

    public BookingResult() {
    }

    public BookingResult(Long flightId,
                         String departureCity,
                         String departureAirport,
                         String destinationCity,
                         String destinationAirport,
                         String departureTime,
                         String arrivalTime,
                         List<String> seatNumbers,
                         String message) {
        this.flightId = flightId;
        this.departureCity = departureCity;
        this.departureAirport = departureAirport;
        this.destinationCity = destinationCity;
        this.destinationAirport = destinationAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seatNumbers = seatNumbers;
        this.message = message;
    }

    public Long getFlightId() {
        return flightId;
    }
    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getDepartureCity() {
        return departureCity;
    }
    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }
    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDestinationCity() {
        return destinationCity;
    }
    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }
    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }
    public void setSeatNumbers(List<String> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

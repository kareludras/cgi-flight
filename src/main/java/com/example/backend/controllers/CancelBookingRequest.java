package com.example.backend.controllers;

public class CancelBookingRequest {
    private Long flightId;

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }
}

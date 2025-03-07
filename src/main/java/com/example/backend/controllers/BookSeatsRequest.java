package com.example.backend.controllers;

import java.util.List;

public class BookSeatsRequest {

    private Long flightId;
    private List<Long> seatIds;

    public BookSeatsRequest() {}

    public Long getFlightId() {
        return flightId;
    }
    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }
    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }
}

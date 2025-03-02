package com.example.backend.models;

import jakarta.persistence.*;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;  // e.g. "1A", "1B" etc.
    private boolean occupied;

    private String seatType;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    public Seat() {
    }

    public Seat(String seatNumber, boolean occupied, String seatType, Flight flight) {
        this.seatNumber = seatNumber;
        this.occupied = occupied;
        this.seatType = seatType;
        this.flight = flight;
    }

    public Long getId() { return id; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }

    public String getSeatType() { return seatType; }
    public void setSeatType(String seatType) { this.seatType = seatType; }

    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }
}

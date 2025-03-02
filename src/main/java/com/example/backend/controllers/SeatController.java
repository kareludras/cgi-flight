package com.example.backend.controllers;

import com.example.backend.models.Seat;
import com.example.backend.services.SeatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // GET all seats
    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }

    // GET seats by flight
    @GetMapping("/flight/{flightId}")
    public List<Seat> getSeatsByFlight(@PathVariable Long flightId) {
        return seatService.getSeatsForFlight(flightId);
    }

    // POST create seat for a given flight
    @PostMapping("/flight/{flightId}")
    public Seat createSeatForFlight(@PathVariable Long flightId, @RequestBody Seat seat) {
        return seatService.createSeatForFlight(flightId, seat);
    }

    // Example: GET recommended seats
    @GetMapping("/recommend/{flightId}")
    public List<Seat> recommendSeats(@PathVariable Long flightId, @RequestParam int quantity) {
        return seatService.recommendSeats(flightId, quantity);
    }
}

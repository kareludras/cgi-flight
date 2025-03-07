package com.example.backend.controllers;

import com.example.backend.models.Flight;
import com.example.backend.services.FlightService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public List<Flight> getFlights(
            @RequestParam(required = false) String departure,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String departureDate,
            @RequestParam(required = false) String flightTime,
            @RequestParam(required = false) String maxPrice
    ) {
        boolean noParams =
                (departure == null || departure.isEmpty()) &&
                        (destination == null || destination.isEmpty()) &&
                        (departureDate == null || departureDate.isEmpty()) &&
                        (flightTime == null || flightTime.isEmpty()) &&
                        (maxPrice == null || maxPrice.isEmpty());

        if (noParams) {
            return flightService.getAllFlights();
        }
        return flightService.filterFlights(departure, destination, departureDate, flightTime, maxPrice);
    }

    @GetMapping("/{id}")
    public Optional<Flight> getFlight(@PathVariable Long id) {
        return flightService.getFlight(id);
    }

    @PostMapping
    public Flight createFlight(@RequestBody Flight flight) {
        return flightService.createFlight(flight);
    }
}

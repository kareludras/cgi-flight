package com.example.backend.services;

import com.example.backend.models.Flight;
import com.example.backend.models.Seat;
import com.example.backend.repositories.FlightRepository;
import com.example.backend.repositories.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;

    public SeatService(SeatRepository seatRepository, FlightRepository flightRepository) {
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
    }

    public List<Seat> getSeatsForFlight(Long flightId) {
        return seatRepository.findByFlightId(flightId);
    }

    public Seat createSeatForFlight(Long flightId, Seat seat) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
        seat.setFlight(flight);
        return seatRepository.save(seat);
    }

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public List<Seat> recommendSeats(Long flightId, int numberOfSeats) {
        // Simple: just find all unoccupied seats for that flight, return the first 'n'
        List<Seat> freeSeats = seatRepository.findByFlightId(flightId)
                .stream()
                .filter(s -> !s.isOccupied())
                .toList();

        if (freeSeats.size() >= numberOfSeats) {
            return freeSeats.subList(0, numberOfSeats);
        }
        // If not enough seats, return empty or partial
        return freeSeats;
    }
}

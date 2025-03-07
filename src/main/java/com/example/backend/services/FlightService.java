package com.example.backend.services;

import com.example.backend.models.Flight;
import com.example.backend.repositories.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> getFlight(Long id) {
        return flightRepository.findById(id);
    }

    public Flight createFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public List<Flight> filterFlights(
            String departure,
            String destination,
            String departureDate,
            String flightTime,
            String maxPrice
    ) {
        double maxP = (maxPrice == null || maxPrice.isEmpty())
                ? Double.MAX_VALUE
                : Double.parseDouble(maxPrice);

        List<Flight> all = flightRepository.findAll();

        return all.stream()
                .filter(f -> {
                    if (departure == null || departure.isEmpty()) {
                        return true;
                    }
                    boolean codeMatch = f.getDepartureAirport().equalsIgnoreCase(departure);
                    boolean cityMatch = f.getDepartureCity().equalsIgnoreCase(departure);
                    return codeMatch || cityMatch;
                })
                .filter(f -> {
                    if (destination == null || destination.isEmpty()) {
                        return true;
                    }
                    boolean matchesCode = f.getDestinationAirport().equalsIgnoreCase(destination);
                    boolean matchesCity = f.getDestinationCity().equalsIgnoreCase(destination);
                    return matchesCode || matchesCity;
                })
                .filter(f -> {
                    if (departureDate == null || departureDate.isEmpty()) {
                        return true;
                    }
                    String flightDate = f.getDepartureTime().toLocalDate().toString();
                    return flightDate.equals(departureDate);
                })
                .filter(f -> {
                    if (flightTime == null || flightTime.isEmpty()) {
                        return true;
                    }
                    String timePart = f.getDepartureTime().toLocalTime().toString().substring(0, 5);
                    return timePart.equals(flightTime);
                })
                .filter(f -> f.getPrice() <= maxP)
                .toList();
    }
}

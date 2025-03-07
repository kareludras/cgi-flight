package com.example.backend.services;

import com.example.backend.controllers.BookingResult;
import com.example.backend.models.Flight;
import com.example.backend.models.Seat;
import com.example.backend.repositories.FlightRepository;
import com.example.backend.repositories.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<Seat> getRecommendedSeats(Long flightId,
                                          int quantity,
                                          Map<String, Integer> preferences,
                                          boolean mustBeAdjacent) {
        List<Seat> availableSeats = seatRepository.findByFlightId(flightId).stream()
                .filter(s -> !s.isOccupied())
                .collect(Collectors.toList());

        if (availableSeats.size() < quantity) {
            return List.of();
        }

        if (mustBeAdjacent && quantity > 1) {
            List<List<Seat>> adjacentGroups = findAllAdjacentGroups(availableSeats, quantity);

            if (adjacentGroups.isEmpty()) {
                return List.of();
            }

            Map<List<Seat>, Integer> groupScores = new HashMap<>();
            for (List<Seat> group : adjacentGroups) {
                int groupScore = group.stream()
                        .mapToInt(seat -> calculateSeatScore(seat, preferences))
                        .sum();
                groupScores.put(group, groupScore);
            }

            return groupScores.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(List.of());
        } else {
            return availableSeats.stream()
                    .sorted(Comparator.comparingInt(seat -> -calculateSeatScore(seat, preferences)))
                    .limit(quantity)
                    .collect(Collectors.toList());
        }
    }

    private int calculateSeatScore(Seat seat, Map<String, Integer> preferences) {
        int score = 0;
        score += 10;
        if (preferences.containsKey("window")) {
            char col = seat.getSeatNumber().charAt(seat.getSeatNumber().length() - 1);
            boolean isWindow = (col == 'A' || col == 'F');
            if (isWindow) {
                score += preferences.get("window");
            }
        }
        if (preferences.containsKey("aisle")) {
            char col = seat.getSeatNumber().charAt(seat.getSeatNumber().length() - 1);
            boolean isAisle = (col == 'C' || col == 'D');
            if (isAisle) {
                score += preferences.get("aisle");
            }
        }
        if (preferences.containsKey("legroom") && seat.getSeatType().toUpperCase().contains("EXTRA_LEGROOM")) {
            score += preferences.get("legroom");
        }
        if (preferences.containsKey("exit_row") && seat.getSeatType().toUpperCase().contains("EXIT_ROW")) {
            score += preferences.get("exit_row");
        }
        if (preferences.containsKey("front")) {
            int row = parseRow(seat);
            int maxRow = 50;
            score += preferences.get("front") * (maxRow - row) / maxRow;
        }
        if (preferences.containsKey("back")) {
            int row = parseRow(seat);
            int maxRow = 50;
            score += preferences.get("back") * row / maxRow;
        }
        return score;
    }

    public List<Seat> recommendSeats(Long flightId,
                                     int quantity,
                                     boolean windowPref,
                                     boolean legroomPref,
                                     boolean exitRowPref,
                                     boolean adjacent) {
        Map<String, Integer> preferences = new HashMap<>();
        if (windowPref) preferences.put("window", 8);
        if (legroomPref) preferences.put("legroom", 8);
        if (exitRowPref) preferences.put("exit_row", 8);
        return getRecommendedSeats(flightId, quantity, preferences, adjacent);
    }

    public BookingResult bookSeats(Long flightId, List<Long> seatIds) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found: " + flightId));

        List<Seat> seatsToBook = seatRepository.findAllById(seatIds);

        for (Seat seat : seatsToBook) {
            if (!seat.getFlight().getId().equals(flightId)) {
                throw new RuntimeException("Seat " + seat.getId() + " does not belong to flight " + flightId);
            }
            if (seat.isOccupied()) {
                throw new RuntimeException("Seat " + seat.getSeatNumber() + " is already occupied!");
            }
        }

        for (Seat seat : seatsToBook) {
            seat.setOccupied(true);
            seat.setUserBooked(true);
            seatRepository.save(seat);
        }

        List<String> seatNumbers = seatsToBook.stream()
                .map(Seat::getSeatNumber)
                .sorted()
                .toList();

        String depTime = (flight.getDepartureTime() != null)
                ? flight.getDepartureTime().toString()
                : "N/A";
        String arrTime = (flight.getArrivalTime() != null)
                ? flight.getArrivalTime().toString()
                : "N/A";

        return new BookingResult(
                flight.getId(),
                flight.getDepartureCity(),
                flight.getDepartureAirport(),
                flight.getDestinationCity(),
                flight.getDestinationAirport(),
                depTime,
                arrTime,
                seatNumbers,
                "Booked " + seatNumbers.size() + " seats successfully."
        );
    }

    public void cancelBookingForFlight(Long flightId) {
        List<Seat> seatsToCancel = seatRepository.findAll().stream()
                .filter(seat -> seat.isOccupied() && seat.isUserBooked() &&
                        seat.getFlight().getId().equals(flightId))
                .toList();
        for (Seat seat : seatsToCancel) {
            seat.setOccupied(false);
            seat.setUserBooked(false);
            seatRepository.save(seat);
        }
    }

    public List<BookingResult> getAllBookedFlights() {
        List<Seat> occupiedSeats = seatRepository.findAll().stream()
                .filter(seat -> seat.isOccupied() && seat.isUserBooked())
                .toList();
        Map<Flight, List<Seat>> seatsByFlight = occupiedSeats.stream()
                .collect(Collectors.groupingBy(Seat::getFlight));
        List<BookingResult> results = new ArrayList<>();
        for (Map.Entry<Flight, List<Seat>> entry : seatsByFlight.entrySet()) {
            Flight flight = entry.getKey();
            List<Seat> seatsForFlight = entry.getValue();
            List<String> seatNumbers = seatsForFlight.stream()
                    .map(Seat::getSeatNumber)
                    .sorted()
                    .toList();
            String depTime = (flight.getDepartureTime() != null)
                    ? flight.getDepartureTime().toString()
                    : "N/A";
            String arrTime = (flight.getArrivalTime() != null)
                    ? flight.getArrivalTime().toString()
                    : "N/A";
            BookingResult booking = new BookingResult(
                    flight.getId(),
                    flight.getDepartureCity(),
                    flight.getDepartureAirport(),
                    flight.getDestinationCity(),
                    flight.getDestinationAirport(),
                    depTime,
                    arrTime,
                    seatNumbers,
                    "User-booked seats for flight " + flight.getId()
            );
            results.add(booking);
        }
        return results;
    }

    private List<List<Seat>> findAllAdjacentGroups(List<Seat> availableSeats, int quantity) {
        List<List<Seat>> adjacentGroups = new ArrayList<>();
        var seatsByRow = availableSeats.stream()
                .collect(Collectors.groupingBy(this::parseRow));
        for (var entry : seatsByRow.entrySet()) {
            List<Seat> rowSeats = entry.getValue();
            rowSeats.sort(Comparator.comparingInt(this::parseColumn));
            for (int start = 0; start <= rowSeats.size() - quantity; start++) {
                boolean isConsecutive = true;
                for (int i = 0; i < quantity - 1; i++) {
                    int col1 = parseColumn(rowSeats.get(start + i));
                    int col2 = parseColumn(rowSeats.get(start + i + 1));
                    if (col2 - col1 != 1) {
                        isConsecutive = false;
                        break;
                    }
                }
                if (isConsecutive) {
                    adjacentGroups.add(new ArrayList<>(rowSeats.subList(start, start + quantity)));
                }
            }
        }
        return adjacentGroups;
    }

    private List<Seat> consecutiveSeats(List<Seat> rowSeats, int quantity) {
        for (int start = 0; start <= rowSeats.size() - quantity; start++) {
            boolean allConsecutive = true;
            for (int i = 0; i < quantity - 1; i++) {
                int col1 = parseColumn(rowSeats.get(start + i));
                int col2 = parseColumn(rowSeats.get(start + i + 1));
                if (col2 - col1 != 1) {
                    allConsecutive = false;
                    break;
                }
            }
            if (allConsecutive) {
                return rowSeats.subList(start, start + quantity);
            }
        }
        return List.of();
    }

    private int parseRow(Seat seat) {
        String seatNumber = seat.getSeatNumber();
        String rowPart = seatNumber.substring(0, seatNumber.length() - 1);
        return Integer.parseInt(rowPart);
    }

    private int parseColumn(Seat seat) {
        char c = seat.getSeatNumber().charAt(seat.getSeatNumber().length() - 1);
        return switch(c) {
            case 'A' -> 1;
            case 'B' -> 2;
            case 'C' -> 3;
            case 'D' -> 4;
            case 'E' -> 5;
            case 'F' -> 6;
            default -> 0;
        };
    }
}

package com.example.backend.controllers;

import com.example.backend.controllers.BookSeatsRequest;
import com.example.backend.controllers.BookingResult;
import com.example.backend.models.Seat;
import com.example.backend.services.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/flight/{flightId}")
    public List<Seat> getSeatsByFlight(@PathVariable Long flightId) {
        return seatService.getSeatsForFlight(flightId);
    }

    @GetMapping("/recommend/{flightId}")
    public List<Seat> recommend(@PathVariable Long flightId,
                                @RequestParam int quantity,
                                @RequestParam(required = false) boolean window,
                                @RequestParam(required = false) boolean extraLegroom,
                                @RequestParam(required = false) boolean exitRow,
                                @RequestParam(required = false) boolean adjacent) {
        return seatService.recommendSeats(flightId, quantity, window, extraLegroom, exitRow, adjacent);
    }

    @PostMapping("/book")
    public BookingResult bookSeats(@RequestBody BookSeatsRequest request) {
        return seatService.bookSeats(request.getFlightId(), request.getSeatIds());
    }

    @GetMapping("/my-bookings")
    public List<BookingResult> getMyBookings() {
        return seatService.getAllBookedFlights();
    }

    @PostMapping("/cancel-booking")
    public ResponseEntity<Map<String, String>> cancelBooking(@RequestBody CancelBookingRequest req) {
        seatService.cancelBookingForFlight(req.getFlightId());
        // Return a small JSON object { "message": "Booking canceled successfully." }
        return ResponseEntity.ok(Map.of("message", "Booking canceled successfully."));
    }
}

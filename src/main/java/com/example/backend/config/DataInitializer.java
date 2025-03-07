package com.example.backend.config;

import com.example.backend.models.Flight;
import com.example.backend.models.Seat;
import com.example.backend.repositories.FlightRepository;
import com.example.backend.repositories.SeatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;

    private static final List<AirportCity> AIRPORTS = List.of(
            new AirportCity("TLL", "Tallinn"),
            new AirportCity("KDL", "Kärdla"),
            new AirportCity("URE", "Kuressaare"),
            new AirportCity("TAY", "Tartu"),
            new AirportCity("RIX", "Riga"),
            new AirportCity("HEL", "Helsinki"),
            new AirportCity("VNO", "Vilnius")
    );

    public DataInitializer(FlightRepository flightRepository, SeatRepository seatRepository) {
        this.flightRepository = flightRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public void run(String... args) {
        int flightCount = 25 + new Random().nextInt(26);

        for (int i = 0; i < flightCount; i++) {
            Flight flight = generateRandomFlight();
            flightRepository.save(flight);

            generateBoeing737Seats(flight);
        }
    }

    private Flight generateRandomFlight() {
        Random rand = new Random();

        AirportCity dep = AIRPORTS.get(rand.nextInt(AIRPORTS.size()));

        AirportCity dest;
        do {
            dest = AIRPORTS.get(rand.nextInt(AIRPORTS.size()));
        } while (dest.equals(dep));

        LocalDateTime now = LocalDateTime.now();
        long randomDays = rand.nextInt(30) + 1;
        int randomHour = rand.nextInt(24);
        int randomMinute = rand.nextInt(60);
        LocalDateTime departureTime = now
                .plusDays(randomDays)
                .withHour(randomHour)
                .withMinute(randomMinute)
                .truncatedTo(ChronoUnit.MINUTES);

        int flightDurationMinutes = 30 + rand.nextInt(151);
        LocalDateTime arrivalTime = departureTime.plusMinutes(flightDurationMinutes);

        double price = 29.99 + rand.nextDouble() * 170.0;
        price = Math.round(price * 100.0) / 100.0;

        Flight flight = new Flight();
        flight.setDepartureAirport(dep.airportCode);
        flight.setDepartureCity(dep.cityName);
        flight.setDestinationAirport(dest.airportCode);
        flight.setDestinationCity(dest.cityName);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setPrice(price);

        return flight;
    }

    private void generateBoeing737Seats(Flight flight) {
        char[] columns = {'A','B','C','D','E','F'};
        Random rand = new Random();

        for (int row = 1; row <= 35; row++) {
            for (char col : columns) {
                Seat seat = new Seat();
                seat.setFlight(flight);
                seat.setSeatNumber(row + String.valueOf(col));
                seat.setOccupied(rand.nextDouble() < 0.3); // ~30% occupied

                // extra legroom rows: 1,16,17
                boolean extraLegroom = (row == 1 || row == 16 || row == 17);
                // exit row rows: 1,2,3,33,34,35
                boolean exitRow = (row == 1 || row == 2 || row == 3 || row == 33 || row == 34 || row == 35);

                if (extraLegroom && exitRow) {
                    seat.setSeatType("EXTRA_LEGROOM_EXIT_ROW");
                } else if (extraLegroom) {
                    seat.setSeatType("EXTRA_LEGROOM");
                } else if (exitRow) {
                    seat.setSeatType("EXIT_ROW");
                } else {
                    seat.setSeatType("REGULAR");
                }

                seatRepository.save(seat);
            }
        }
    }

    private static record AirportCity(String airportCode, String cityName) {}
}

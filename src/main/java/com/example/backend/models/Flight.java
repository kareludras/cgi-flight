package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departureAirport;
    private String departureCity;

    private String destinationAirport;
    private String destinationCity;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Double price;

    public Flight() {}

    public Flight(String departureAirport, String departureCity,
                  String destinationAirport, String destinationCity,
                  LocalDateTime departureTime, LocalDateTime arrivalTime,
                  Double price) {
        this.departureAirport = departureAirport;
        this.departureCity = departureCity;
        this.destinationAirport = destinationAirport;
        this.destinationCity = destinationCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
    }


    public Long getId() {
        return id;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    @JsonProperty("formattedDepartureTime")
    public String getFormattedDepartureTime() {
        if (departureTime == null) {
            return "";
        }
        return departureTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm"));
    }

    @JsonProperty("formattedArrivalTime")
    public String getFormattedArrivalTime() {
        if (arrivalTime == null) {
            return "";
        }
        return arrivalTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm"));
    }

    @JsonProperty("formattedTimeRange")
    public String getFormattedTimeRange() {
        if (departureTime == null || arrivalTime == null) {
            return "";
        }

        if (departureTime.toLocalDate().equals(arrivalTime.toLocalDate())) {
            return departureTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm")) +
                    " - " +
                    arrivalTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            return getFormattedDepartureTime() + " - " + getFormattedArrivalTime();
        }
    }
}
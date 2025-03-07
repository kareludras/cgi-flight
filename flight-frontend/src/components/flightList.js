import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import FlightFilter from "./flightFilter";
import "../styles/flightList.css";

const cityImages = {
    "Tallinn": "/images/tallinn.jpg",
    "Riga": "/images/riga.jpg",
    "Kärdla": "/images/kardla.jpg",
    "Kuressaare": "/images/kuressaare.jpg",
    "Tartu": "/images/tartu.jpg",
    "Helsinki": "/images/helsinki.jpg",
    "Vilnius": "/images/vilnius.jpg"
};

function getCityImage(city) {
    return cityImages[city] || "/images/fallback.jpg";
}

function FlightList() {
    const [flights, setFlights] = useState([]);
    const [filters, setFilters] = useState({});
    const navigate = useNavigate();

    useEffect(() => {
        fetchFlights(filters);
    }, [filters]);

    const fetchFlights = (filterObj) => {
        if (!filterObj.departure || filterObj.departure.trim() === "") {
            setFlights([]);
            return;
        }

        const params = new URLSearchParams();

        if (filterObj.departure) params.append("departure", filterObj.departure);

        if (filterObj.destination) params.append("destination", filterObj.destination);
        if (filterObj.departureDate) params.append("departureDate", filterObj.departureDate);
        if (filterObj.flightTime) params.append("flightTime", filterObj.flightTime);
        if (filterObj.maxPrice) params.append("maxPrice", filterObj.maxPrice);

        fetch(`/api/flights?${params.toString()}`)
            .then((res) => res.json())
            .then((data) => setFlights(data))
            .catch((err) => console.error("Error fetching flights:", err));
    };

    const handleFilter = (filterObj) => {
        setFilters(filterObj);
    };

    return (
        <div style={{ padding: "20px" }}>
            <h1>Select Flight</h1>
            <FlightFilter onFilter={handleFilter} />

            <h2>Flight List</h2>

            <div style={{
                display: "flex",
                flexWrap: "wrap",
                gap: "20px"
            }}>
                {flights.map((flight) => (
                    <div
                        key={flight.id}
                        style={{
                            border: "1px solid #ccc",
                            borderRadius: "6px",
                            width: "300px",
                            height: "420px",
                            display: "flex",
                            flexDirection: "column",
                            justifyContent: "space-between",
                            overflow: "hidden",
                            padding: "10px"
                        }}
                        className="booking-card"
                    >
                        <div style={{ width: "100%", height: "150px", marginBottom: "10px" }}>
                            <img
                                src={getCityImage(flight.destinationCity)}
                                alt={flight.destinationCity}
                                style={{
                                    width: "100%",
                                    height: "100%",
                                    objectFit: "cover"
                                }}
                            />
                        </div>

                        <div style={{ flex: "1 0 auto" }}>
                            <p><strong>Flight Number:</strong> {flight.id}</p>
                            <p>
                                <strong>From:</strong>{" "}
                                {flight.departureCity} ({flight.departureAirport})<br />
                                <strong>Departure:</strong>{" "}
                                {flight.formattedDepartureTime}
                            </p>
                            <p>
                                <strong>To:</strong>{" "}
                                {flight.destinationCity} ({flight.destinationAirport})<br/>
                                <strong>Arrival:</strong>{" "}
                                {flight.formattedArrivalTime}
                            </p>
                            <p><strong>Price:</strong> {flight.price}</p>
                        </div>

                        {/* Button at the bottom */}
                        <div style={{ marginTop: "10px" }}>
                            <button
                                className="btn"
                                onClick={() => navigate(`/flight/${flight.id}/seats`)}
                                style={{ width: "100%" }}
                            >
                                Select Seats
                            </button>
                        </div>
                    </div>
                ))}

                {flights.length === 0 && (
                    <p>No flights found. Make sure you chose a departure.</p>
                )}
            </div>
        </div>
    );
}

export default FlightList;

import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/flightFilter.css";

const AIRPORTS = [
    { code: "TLL", city: "Tallinn" },
    { code: "KDL", city: "Kärdla" },
    { code: "URE", city: "Kuressaare" },
    { code: "TAY", city: "Tartu" },
    { code: "RIX", city: "Riga" },
    { code: "HEL", city: "Helsinki" },
    { code: "VNO", city: "Vilnius" }
];

function FlightFilter({ onFilter }) {
    const [departure, setDeparture] = useState("");
    const [destination, setDestination] = useState("");
    const [departureDate, setDepartureDate] = useState("");
    const [flightTime, setFlightTime] = useState("");
    const [maxPrice, setMaxPrice] = useState("");

    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        const filters = {
            departure,
            destination,
            departureDate,
            flightTime,
            maxPrice
        };
        onFilter(filters);
    };

    return (
        <form onSubmit={handleSubmit} className="form-group" style={{ marginBottom: "20px" }}>
            <h3>Filter Flights</h3>

            <div className="form-group">
                <label>Departure (required):</label>
                <select value={departure} onChange={(e) => setDeparture(e.target.value)} required>
                    <option value="">-- Select Departure --</option>
                    {AIRPORTS.map((airport) => (
                        <option key={airport.code} value={airport.code}>
                            {airport.code} ({airport.city})
                        </option>
                    ))}
                </select>
            </div>

            <div className="form-group">
                <label>Destination:</label>
                <select value={destination} onChange={(e) => setDestination(e.target.value)}>
                    <option value="">-- Any --</option>
                    {AIRPORTS.map((airport) => (
                        <option key={airport.code} value={airport.code}>
                            {airport.code} ({airport.city})
                        </option>
                    ))}
                </select>
            </div>

            <div className="form-group">
                <label>Departure Date:</label>
                <input type="date" value={departureDate} onChange={(e) => setDepartureDate(e.target.value)} />
            </div>

            <div className="form-group">
                <label>Flight Time:</label>
                <input type="time" value={flightTime} onChange={(e) => setFlightTime(e.target.value)} />
            </div>

            <div className="form-group">
                <label>Max Price:</label>
                <input type="number" value={maxPrice} onChange={(e) => setMaxPrice(e.target.value)} />
            </div>

            <button type="submit" className="btn">Search</button>
            <button
                type="button"
                onClick={() => navigate("/my-bookings")}
                className="btn"
                style={{ marginLeft: "10px" }}
            >
                View My Bookings
            </button>
        </form>
    );
}

export default FlightFilter;

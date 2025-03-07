import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../index.css";
import "../styles/myBookings.css";

function MyBookings() {
    const [bookings, setBookings] = useState([]);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const fetchMyBookings = () => {
        fetch("/api/seats/my-bookings")
            .then((res) => {
                if (!res.ok) throw new Error("Failed to fetch bookings");
                return res.json();
            })
            .then((data) => {
                setBookings(data);
                setError("");
            })
            .catch((err) => {
                console.error("Error fetching my bookings:", err);
                setError("Failed to load bookings.");
            });
    };

    useEffect(() => {
        fetchMyBookings();
    }, []);

    const handleCancelBooking = (flightId) => {
        fetch("/api/seats/cancel-booking", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ flightId }),
        })
            .then((res) => {
                if (!res.ok) {
                    throw new Error("Failed to cancel booking");
                }
                return res.json();
            })
            .then((data) => {
                console.log("Cancel booking success:", data.message);
                fetchMyBookings();
            })
            .catch((err) => {
                console.error("Error canceling booking:", err);
                setError("Failed to cancel booking.");
            });
    };

    return (
        <div style={{ padding: "20px" }}>
            <div
                style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "space-between",
                    marginBottom: "10px"
                }}
            >
                <h2 style={{ margin: 0 }}>My Bookings</h2>
                <button className="btn" onClick={() => navigate("/")}>
                    Back to Main Page
                </button>
            </div>

            {error && <p style={{ color: "red" }}>{error}</p>}

            {bookings.map((b) => (
                <div
                    className="booking-card"
                    key={b.flightId}
                    style={{
                        border: "1px solid #ccc",
                        margin: "10px 0",
                        padding: "10px"
                    }}
                >
                    <p><strong>Flight:</strong> {b.flightId}</p>
                    <p><strong>From:</strong> {b.departureCity} ({b.departureAirport})</p>
                    <p><strong>To:</strong> {b.destinationCity} ({b.destinationAirport})</p>
                    <p><strong>Departure Time:</strong> {b.departureTime}</p>
                    <p><strong>Arrival Time:</strong> {b.arrivalTime}</p>
                    <p><strong>Seats:</strong> {b.seatNumbers.join(", ")}</p>
                    <p>{b.message}</p>

                    <button
                        className="btn"
                        onClick={() => handleCancelBooking(b.flightId)}
                        style={{ marginTop: "10px" }}
                    >
                        Cancel Booking
                    </button>
                </div>
            ))}

            {bookings.length === 0 && !error && (
                <p>No bookings found.</p>
            )}
        </div>
    );
}

export default MyBookings;

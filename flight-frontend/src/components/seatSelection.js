import React, { useEffect, useState } from "react"
import { useParams, useNavigate } from "react-router-dom"
import "../styles/seatSelection.css"

function SeatSelection() {
    const { flightId } = useParams()
    const navigate = useNavigate()
    const [seats, setSeats] = useState([])
    const [recommended, setRecommended] = useState([])
    const [quantity, setQuantity] = useState(1)
    const [windowPref, setWindowPref] = useState(false)
    const [legroomPref, setLegroomPref] = useState(false)
    const [exitRowPref, setExitRowPref] = useState(false)
    const [adjacentPref, setAdjacentPref] = useState(false)
    const [selectedSeatIds, setSelectedSeatIds] = useState([])
    const [error, setError] = useState("")

    useEffect(() => {
        fetch(`/api/seats/flight/${flightId}`)
            .then((res) => res.json())
            .then((data) => setSeats(data))
            .catch((err) => console.error("Error fetching seats:", err))
    }, [flightId])

    const handleRecommend = () => {
        const params = new URLSearchParams()
        params.append("quantity", quantity)
        if (windowPref) params.append("window", "true")
        if (legroomPref) params.append("extraLegroom", "true")
        if (exitRowPref) params.append("exitRow", "true")
        if (adjacentPref) params.append("adjacent", "true")
        fetch(`/api/seats/recommend/${flightId}?${params.toString()}`)
            .then((res) => res.json())
            .then((data) => {
                setRecommended(data)
                setError("")
            })
            .catch((err) => {
                console.error("Error recommending seats:", err)
                setError("Failed to recommend seats.")
            })
    }

    const handleSeatClick = (seat) => {
        if (seat.occupied) {
            setError("This seat is already taken!")
            return
        }
        if (selectedSeatIds.includes(seat.id)) {
            setSelectedSeatIds(selectedSeatIds.filter((id) => id !== seat.id))
        } else {
            setSelectedSeatIds([...selectedSeatIds, seat.id])
        }
        setError("")
    }

    const handleConfirm = () => {
        const seatsToBook =
            selectedSeatIds.length > 0 ? selectedSeatIds : recommended.map((s) => s.id)
        if (seatsToBook.length < quantity) {
            setError(`You must select at least ${quantity} seats!`)
            return
        }
        fetch(`/api/seats/book`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                flightId: parseInt(flightId, 10),
                seatIds: seatsToBook
            })
        })
            .then((res) => {
                if (!res.ok) throw new Error("Failed to book seats")
                return res.json()
            })
            .then((result) => {
                setSeats((prevSeats) =>
                    prevSeats.map((seat) =>
                        seatsToBook.includes(seat.id)
                            ? { ...seat, occupied: true }
                            : seat
                    )
                )
                setRecommended([])
                setSelectedSeatIds([])
                setError("")
                navigate("/my-bookings")
            })
            .catch((err) => {
                console.error("Error booking seats:", err)
                setError("Booking failed. Please try again.")
            })
    }

    const isRecommended = (seatId) => recommended.some((r) => r.id === seatId)
    const isSelected = (seatId) => selectedSeatIds.includes(seatId)

    return (
        <div className="seat-selection-container">
            <h2>Seat Selection for Flight #{flightId}</h2>
            {error && <p className="error-message">{error}</p>}
            <div className="seat-preferences">
                <div className="pref-field">
                    <label>Quantity of Tickets:</label>
                    <input
                        type="number"
                        min={1}
                        value={quantity}
                        onChange={(e) => setQuantity(e.target.value)}
                    />
                </div>
                <div className="pref-field checkbox-field">
                    <input
                        type="checkbox"
                        id="windowPref"
                        checked={windowPref}
                        onChange={(e) => setWindowPref(e.target.checked)}
                    />
                    <label htmlFor="windowPref">Window Seat</label>
                </div>
                <div className="pref-field checkbox-field">
                    <input
                        type="checkbox"
                        id="legroomPref"
                        checked={legroomPref}
                        onChange={(e) => setLegroomPref(e.target.checked)}
                    />
                    <label htmlFor="legroomPref">
                        Extra Legroom <span className="pref-badge-xl">XL</span>
                    </label>
                </div>
                <div className="pref-field checkbox-field">
                    <input
                        type="checkbox"
                        id="exitRowPref"
                        checked={exitRowPref}
                        onChange={(e) => setExitRowPref(e.target.checked)}
                    />
                    <label htmlFor="exitRowPref">
                        Exit Row <span className="pref-badge-ex">EX</span>
                    </label>
                </div>
                <div className="pref-field checkbox-field">
                    <input
                        type="checkbox"
                        id="adjacentPref"
                        checked={adjacentPref}
                        onChange={(e) => setAdjacentPref(e.target.checked)}
                    />
                    <label htmlFor="adjacentPref">Adjacent Seats</label>
                </div>
            </div>
            <button className="btn" style={{ marginBottom: "20px" }} onClick={handleRecommend}>
                Recommend Seats
            </button>
            <hr />
            <div className="seat-map-section">
                <h3 className="seat-map-title">Seat Map</h3>
                <div className="seat-map-container">
                    {seats.map((seat) => {
                        let seatClass = "seat-square"
                        if (seat.occupied) {
                            seatClass += " occupied"
                        } else if (isSelected(seat.id)) {
                            seatClass += " selected"
                        } else if (isRecommended(seat.id)) {
                            seatClass += " recommended"
                        }
                        if (seat.seatType && seat.seatType.includes("EXTRA_LEGROOM")) {
                            seatClass += " extra-legroom"
                        }
                        if (seat.seatType && seat.seatType.includes("EXIT_ROW")) {
                            seatClass += " exit-row"
                        }
                        return (
                            <div
                                key={seat.id}
                                className={seatClass}
                                onClick={() => !seat.occupied && handleSeatClick(seat)}
                            >
                                {seat.seatNumber}
                            </div>
                        )
                    })}
                </div>
            </div>
            <div>
                <h4>Recommended seats:</h4>
                {recommended.length > 0 ? (
                    <ul>
                        {recommended.map((s) => (
                            <li key={s.id}>{s.seatNumber}</li>
                        ))}
                    </ul>
                ) : (
                    <p>None</p>
                )}
                <h4>Manually selected seats:</h4>
                {selectedSeatIds.length > 0 ? (
                    <ul>
                        {selectedSeatIds.map((id) => {
                            const seatObj = seats.find((s) => s.id === id)
                            return seatObj ? <li key={id}>{seatObj.seatNumber}</li> : null
                        })}
                    </ul>
                ) : (
                    <p>None</p>
                )}
            </div>
            <div className="button-row">
                <button className="btn" onClick={handleConfirm}>
                    Confirm
                </button>
                <button className="btn" onClick={() => navigate("/")}>
                    Cancel
                </button>
            </div>
        </div>
    )
}

export default SeatSelection

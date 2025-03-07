import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import SeatSelection from "./components/seatSelection.js";
import FlightList from "./components/flightList.js";
import MyBookings from "./components/myBookings.js";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<FlightList />} />
                <Route path="/flight/:flightId/seats" element={<SeatSelection />} />
                <Route path="/my-bookings" element={<MyBookings />} />
            </Routes>
        </Router>
    );
}

export default App;

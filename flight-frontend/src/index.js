import React from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import SeatSelection from "./components/seatSelection.js";
import FlightList from "./components/flightList.js";
import "./index.css";
import MyBookings from "./components/myBookings";

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

const container = document.getElementById("root");
const root = createRoot(container);
root.render(<App />);
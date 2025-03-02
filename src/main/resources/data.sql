INSERT INTO FLIGHT (id, departure_airport, destination_airport, departure_time, arrival_time, price)
VALUES (1, 'TLL', 'RIX', '2025-03-10 08:00:00', '2025-03-10 09:00:00', 49.99);

INSERT INTO FLIGHT (id, departure_airport, destination_airport, departure_time, arrival_time, price)
VALUES (2, 'TLL', 'HEL', '2025-03-11 12:00:00', '2025-03-11 13:30:00', 59.99);

INSERT INTO SEAT (id, seat_number, occupied, seat_type, flight_id)
VALUES (1, '1A', false, 'WINDOW', 1);

INSERT INTO SEAT (id, seat_number, occupied, seat_type, flight_id)
VALUES (2, '1B', true, 'REGULAR', 1);

INSERT INTO SEAT (id, seat_number, occupied, seat_type, flight_id)
VALUES (3, '1C', false, 'REGULAR', 1);

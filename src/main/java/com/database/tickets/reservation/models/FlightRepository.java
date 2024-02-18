package com.database.tickets.reservation.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    List<Flight> findByDepartureAirportAndArrivalAirport(int departureAirport, int arrivalAirport);
    List<Flight> findByDepartureDate(LocalDate departureDate);
    Flight findFlightByIdFlight(int idFlight);
}

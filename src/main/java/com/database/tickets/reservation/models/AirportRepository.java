package com.database.tickets.reservation.models;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Integer> {
    Airport findByName(String name);
}

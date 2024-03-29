package com.database.tickets.reservation.models;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, Integer>{
    Plane findByPlaneName(String planeName);
    Plane findPlaneByIdPlane(int idPlane);
}

package com.database.tickets.reservation.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="planes")
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPlane;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String planeName;

    @Getter
    @Setter
    @Column(nullable = false)
    private int seatsEconomic;

    @Getter
    @Setter
    @Column(nullable = false)
    private int seatsBussines;

    public Plane(String planeName, int seatsEconomic, int seatsBussines){
        this.planeName = planeName;
        this.seatsEconomic = seatsEconomic;
        this.seatsBussines = seatsBussines;
    }

    public Plane(){}
}

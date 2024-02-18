package com.database.tickets.reservation.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name="airports")
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAirport;

    @Setter
    private String name;

    public Airport(){}

    public Airport(String name){
        this.name = name;
    }
}

package com.database.tickets.reservation.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Table(name="flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFlight;

    @Setter
    @Column(nullable = false)
    private int idPlane;

    @Setter
    @Column(nullable = false)
    private LocalDate departureDate;

    @Setter
    @Column(nullable = false)
    private LocalDate arrivalDate;

    @Setter
    @Column(nullable = false)
    private LocalTime departureTime;

    @Setter
    @Column(nullable = false)
    private LocalTime arrivalTime;

    @Setter
    @Column(nullable = false)
    private int departureAirport;

    @Setter
    @Column(nullable = false)
    private int arrivalAirport;

    public Flight(){}

    public  Flight(int idPlane, LocalDate departureDate, LocalDate arrivalDate,
                   LocalTime departureTime, LocalTime arrivalTime,
                   int departureAirport, int arrivalAirport){
        this.idPlane = idPlane;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
    }


}

package com.database.tickets.reservation.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name="reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReservation;

    @Setter
    @Column(nullable = false)
    private int idUser;

    @Setter
    @Column(nullable = false)
    private int idFlight;

    @Setter
    @Column(nullable = false)
    private int seatNumber;

    @Setter
    @Column(nullable = false)
    private String seatClass;

    public Reservation(){}

    public Reservation(int idUser, int idFlight, int seatNumber, String seatClass){
        this.idUser = idUser;
        this.idFlight = idFlight;
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
    }
}

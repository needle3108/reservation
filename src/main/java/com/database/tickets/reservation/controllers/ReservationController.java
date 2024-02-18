package com.database.tickets.reservation.controllers;

import com.database.tickets.reservation.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ReservationController {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PlaneRepository planeRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @RequestMapping(value="buyEconomic", method = RequestMethod.GET)
    public String buyEconomic(@RequestParam("id") int id, Model model){
        List<Integer> availableSeats = getSeats("economic", id);

        model.addAttribute("seats", availableSeats);

        return "/buyEconomic";
    }

    @RequestMapping(value="buyBusiness", method = RequestMethod.GET)
    public String buyBusiness(@RequestParam("id") int id, Model model){
        List<Integer> availableSeats = getSeats("business", id);

        model.addAttribute("seats", availableSeats);

        return "/buyBusiness";
    }

    private List<Integer> getSeats(String seatsClass, int id){
        Flight flight = flightRepository.findFlightByIdFlight(id);
        int idPlane = flight.getIdPlane();
        int numberSeats;

        Plane plane = planeRepository.findPlaneByIdPlane(idPlane);
        if(seatsClass.equals("economic")){
            numberSeats = plane.getSeatsEconomic();
        }
        else{
            numberSeats = plane.getSeatsBussines();
        }

        List<Integer> seats = new ArrayList<>();
        List<Reservation> reservations = reservationRepository.findAllByIdFlightAndSeatClass(id, seatsClass);

        for(int i = 1; i <= numberSeats; i++){
            boolean isTaken = false;
            for(Reservation reservation : reservations){
                if(reservation.getSeatNumber() == i){ isTaken = true; break;}
            }
            if(!isTaken){seats.add(i);}
        }

        return seats;
    }
}

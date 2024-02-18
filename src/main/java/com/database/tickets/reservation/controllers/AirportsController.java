package com.database.tickets.reservation.controllers;

import com.database.tickets.reservation.models.Airport;
import com.database.tickets.reservation.models.AirportRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AirportsController {
    @Autowired
    private AirportRepository airportRepository;

    @GetMapping("/airports")
    public String airports(Model model){
        model.addAttribute("airports", airportRepository.findAll());
        return "/airports";
    }

    @GetMapping("addAirport")
    public String addAirport(){
        return "/addAirport";
    }

    @PostMapping("/addAirport/save")
    public String addPlane(@RequestParam Map<String, String> newAirport, HttpServletResponse response){
        System.out.println("ADD airport");

        String newAirportName = newAirport.get("name");

        Airport existingAirport = airportRepository.findByName(newAirportName);

        if(existingAirport != null){
            response.setStatus(401);
            return "/addAirport";
        }

        airportRepository.save(new Airport(newAirportName));
        response.setStatus(201);
        return "/addedAirport";
    }
}

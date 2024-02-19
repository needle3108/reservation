package com.database.tickets.reservation.controllers;

import com.database.tickets.reservation.models.Airport;
import com.database.tickets.reservation.models.AirportRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    public String airports(Model model, HttpSession session){
        if(isUserLogged(session)){
            if(!session.getAttribute("username").equals("admin")){
                return "/index";
            }
        }
        else{
            return "/index";
        }
        model.addAttribute("airports", airportRepository.findAll());
        return "/admin/airports";
    }

    @GetMapping("addAirport")
    public String addAirport(HttpSession session){
        if(isUserLogged(session)){
            if(!session.getAttribute("username").equals("admin")){
                return "/index";
            }
        }
        else{
            return "/index";
        }

        return "/admin/addAirport";
    }

    @GetMapping("/searchAirports")
    public String searchAirport(Model model){
        model.addAttribute("airports", airportRepository.findAll());
        return "/searchAirports";
    }

    @GetMapping("/searchAirportsLogged")
    public String searchAirportLogged(Model model, HttpSession session){
        if(!isUserLogged(session)){ return "/index"; }

        model.addAttribute("airports", airportRepository.findAll());
        return "/user/searchAirportsLogged";
    }

    @PostMapping("/addAirport/save")
    public String addPlane(@RequestParam Map<String, String> newAirport, HttpServletResponse response, HttpSession session){
        if(isUserLogged(session)){
            if(!session.getAttribute("username").equals("admin")){
                return "/index";
            }
        }
        else{
            return "/index";
        }

        String newAirportName = newAirport.get("name");

        Airport existingAirport = airportRepository.findByName(newAirportName);

        if(existingAirport != null){
            response.setStatus(401);
            return "/admin/addAirport";
        }

        airportRepository.save(new Airport(newAirportName));
        response.setStatus(201);
        return "/admin/addedAirport";
    }

    private boolean isUserLogged(HttpSession session){
        try{
            String isLogged = session.getAttribute("username").toString();
            return true;
        }
        catch(NullPointerException e){
            return false;
        }
    }
}

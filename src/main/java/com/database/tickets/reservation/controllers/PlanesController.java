package com.database.tickets.reservation.controllers;

import com.database.tickets.reservation.models.Plane;
import com.database.tickets.reservation.models.PlaneRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class PlanesController {
    @Autowired
    private PlaneRepository planeRepository;

    @GetMapping("/planes")
    public String planes(Model model){
        model.addAttribute("planes", planeRepository.findAll());
        return "/planes";
    }

    @GetMapping("/addPlane")
    public String addPlane(){
        return "/addPlane";
    }

    @PostMapping("/addPlane/save")
    public String addPlane(@RequestParam Map<String, String> newPlane, HttpServletResponse response){
        System.out.println("ADD plane");

        String newPlaneName = newPlane.get("planeName");
        int newSeatsEconomic = Integer.parseInt(newPlane.get("seatsEconomic"));
        int newSeatsBusiness = Integer.parseInt(newPlane.get("seatsBusiness"));

        Plane existingPlane = planeRepository.findByPlaneName(newPlaneName);

        if(existingPlane != null){
            response.setStatus(401);
            return "/addPlane";
        }

        planeRepository.save(new Plane(newPlaneName, newSeatsEconomic, newSeatsBusiness));
        response.setStatus(201);
        return "/addedPlane";
    }

}

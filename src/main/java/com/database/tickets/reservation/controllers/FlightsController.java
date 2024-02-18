package com.database.tickets.reservation.controllers;

import com.database.tickets.reservation.models.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class FlightsController {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PlaneRepository planeRepository;
    @Autowired
    private AirportRepository airportRepository;

    @GetMapping("/flights")
    public String flights(Model model){
        model.addAttribute("flights", flightRepository.findAll());
        return "/flights";
    }

    @GetMapping("/addFlight")
    public String addFlight(Model model){
        model.addAttribute("planes", planeRepository.findAll());
        model.addAttribute("airports", airportRepository.findAll());
        return "/addFlight";
    }

    @PostMapping("/addFlight/save")
    public String addPlane(@RequestParam Map<String, String> newFlight, HttpServletResponse response){
        int newIdPlane = Integer.parseInt(newFlight.get("idPlane"));

        int departureYear = Integer.parseInt(newFlight.get("departureYear"));
        int departureMonth = Integer.parseInt(newFlight.get("departureMonth"));
        int departureDay = Integer.parseInt(newFlight.get("departureDay"));
        LocalDate newDepartureDate = LocalDate.of(departureYear,
                departureMonth,departureDay);

        int arrivalYear = Integer.parseInt(newFlight.get("arrivalYear"));
        int arrivalMonth = Integer.parseInt(newFlight.get("arrivalMonth"));
        int arrivalDay = Integer.parseInt(newFlight.get("arrivalDay"));
        LocalDate newArrivalDate = LocalDate.of(arrivalYear,
                arrivalMonth, arrivalDay);

        int departureHour = Integer.parseInt(newFlight.get("departureHour"));
        int departureMinutes = Integer.parseInt(newFlight.get("departureMinutes"));
        LocalTime newDepartureTime = LocalTime.of(departureHour, departureMinutes);

        int arrivalHour = Integer.parseInt(newFlight.get("arrivalHour"));
        int arrivalMinutes = Integer.parseInt(newFlight.get("arrivalMinutes"));
        LocalTime newArrivalTime = LocalTime.of(arrivalHour, arrivalMinutes);

        int newDepartureAirport = Integer.parseInt(newFlight.get("departureAirport"));
        int newArrivalAirport = Integer.parseInt(newFlight.get("arrivalAirport"));


        flightRepository.save(new Flight(newIdPlane, newDepartureDate, newArrivalDate,
                newDepartureTime, newArrivalTime, newDepartureAirport, newArrivalAirport));
        response.setStatus(201);
        return "/addedFlight";
    }

    @PostMapping("/searchAirport/search")
    public String searchAirport(@RequestParam Map<String, String> newFlight, HttpServletResponse response, Model model,
                                HttpSession session){
        int departureAirport = Integer.parseInt(newFlight.get("departureAirport"));
        int arrivalAirport = Integer.parseInt(newFlight.get("arrivalAirport"));

        List<Flight> flights = new ArrayList<>(flightRepository.findByDepartureAirportAndArrivalAirport(departureAirport,
                arrivalAirport));

        List<Airport> airports = getAllAirports();

        List<String> airportsNames = getAirports(flights, airports);

        model.addAttribute("flights", flights);
        model.addAttribute("names", airportsNames);

        try{
            session.getAttribute("username");
            response.setStatus(201);
            return "/foundedFlightsLogged";
        }
        catch(NullPointerException e){
            response.setStatus(201);
            return "/foundedFlights";
        }
    }

    private static List<String> getAirports(List<Flight> flights, List<Airport> airports) {
        List<String> airportsNames = new ArrayList<>();
        int index = 0;

        for(Flight flight : flights){
            for(Airport airport : airports){
                if(airport.getIdAirport() == flight.getDepartureAirport()){
                    airportsNames.add(index,airport.getName());
                    index++;
                }
            }

            for(Airport airport : airports){
                if(airport.getIdAirport() == flight.getArrivalAirport()){
                    airportsNames.add(index,airport.getName());
                    index++;
                }
            }
        }
        return airportsNames;
    }

    @GetMapping("/searchDate")
    public String searchDate(){
        return "/searchDate";
    }

    @PostMapping("/searchDate/search")
    public String searchDateResult(@RequestParam Map<String, String> newFlight, HttpServletResponse response, Model model,
                                   HttpSession session){
        int departureYear = Integer.parseInt(newFlight.get("departureYear"));
        int departureMonth = Integer.parseInt(newFlight.get("departureMonth"));
        int departureDay = Integer.parseInt(newFlight.get("departureDay"));
        LocalDate newDepartureDate = LocalDate.of(departureYear,
                departureMonth,departureDay);

        List<Flight> flights = new ArrayList<>(flightRepository.findByDepartureDate(newDepartureDate));

        List<Airport> airports = getAllAirports();

        List<String> airportsNames = getAirports(flights, airports);

        model.addAttribute("flights", flights);
        model.addAttribute("names", airportsNames);

        try{
           session.getAttribute("username");
           response.setStatus(201);
           return "/foundedFlightsLogged";
        }
        catch(NullPointerException e){
            response.setStatus(201);
            return "/foundedFlights";
        }
    }

    private List<Airport> getAllAirports(){
        return airportRepository.findAll();
    }

    @GetMapping("/searchDateLogged")
    public String searchDateLogged(){
        return "/searchDateLogged";
    }

    @GetMapping("/buyTickets")
    public String buyTickets(){
        return "/buyTickets";
    }
}

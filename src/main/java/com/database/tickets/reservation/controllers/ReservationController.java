package com.database.tickets.reservation.controllers;

import com.database.tickets.reservation.models.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ReservationController {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PlaneRepository planeRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AirportRepository airportRepository;

    @RequestMapping(value="buyEconomic", method = RequestMethod.GET)
    public String buyEconomic(@RequestParam("id") int id, Model model){
        List<Integer> availableSeats = getSeats("economic", id);

        model.addAttribute("seats", availableSeats);
        model.addAttribute("idFlight", id);

        return "/buyEconomic";
    }

    @RequestMapping(value="buyBusiness", method = RequestMethod.GET)
    public String buyBusiness(@RequestParam("id") int id, Model model){
        List<Integer> availableSeats = getSeats("business", id);

        model.addAttribute("seats", availableSeats);
        model.addAttribute("idFlight", id);

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

    @RequestMapping(value="buyEconomic/make", method = RequestMethod.POST)
    public String buyEconomicMake(@RequestParam Map<String,String> seat, @RequestParam("id") int id,
                                  HttpServletResponse response,  HttpSession session){
        int chosenSeat = Integer.parseInt(seat.get("seat"));

        User currentUser = userRepository.findUserByUsername(session.getAttribute("username").toString());

        reservationRepository.save(new Reservation(currentUser.getId(), id, chosenSeat, "economic"));
        response.setStatus(201);

        return "/success";
    }

    @RequestMapping(value="buyBusiness/make", method = RequestMethod.POST)
    public String buyBusinessMake(@RequestParam Map<String,String> seat, @RequestParam("id") int id,
                                  HttpServletResponse response,  HttpSession session){
        int chosenSeat = Integer.parseInt(seat.get("seat"));

        User currentUser = userRepository.findUserByUsername(session.getAttribute("username").toString());

        reservationRepository.save(new Reservation(currentUser.getId(), id, chosenSeat, "business"));
        response.setStatus(201);

        return "/success";
    }

    @GetMapping("/userProfile")
    public String userProfile(HttpSession session, Model model){
        User currentUser = userRepository.findUserByUsername(session.getAttribute("username").toString());
        List<Reservation> userReservations = reservationRepository.findAllByIdUser(currentUser.getId());

        List<LocalDate> departureDates = new ArrayList<>();
        List<LocalDate> arrivalDates = new ArrayList<>();

        List<LocalTime> departureTimes = new ArrayList<>();
        List<LocalTime> arrivalTimes = new ArrayList<>();

        List<String> departureAirports = new ArrayList<>();
        List<String> arrivalAirports = new ArrayList<>();

        List<Integer> seats = new ArrayList<>();
        List<String> seatsClasses = new ArrayList<>();

        for(Reservation userReservation : userReservations){
            int idFlight = userReservation.getIdFlight();
            Flight flight = flightRepository.findFlightByIdFlight(idFlight);

            departureDates.add(flight.getDepartureDate());
            arrivalDates.add(flight.getArrivalDate());

            departureTimes.add(flight.getDepartureTime());
            arrivalTimes.add(flight.getArrivalTime());

            Airport airportDeparture = airportRepository.findAirportByIdAirport(flight.getDepartureAirport());
            departureAirports.add(airportDeparture.getName());

            Airport airportArrival = airportRepository.findAirportByIdAirport(flight.getArrivalAirport());
            arrivalAirports.add(airportArrival.getName());

            seats.add(userReservation.getSeatNumber());
            seatsClasses.add(userReservation.getSeatClass());
        }

        model.addAttribute("departureAirports", departureAirports);
        model.addAttribute("arrivalAirports", arrivalAirports);
        model.addAttribute("departureDates", departureDates);
        model.addAttribute("arrivalDates", arrivalDates);
        model.addAttribute("departureTimes", departureTimes);
        model.addAttribute("arrivalTimes", arrivalTimes);
        model.addAttribute("seats", seats);
        model.addAttribute("seatClasses", seatsClasses);

        return "/userProfile";
    }

    @RequestMapping(value="seeReservations", method = RequestMethod.GET)
    public String seeReservations(@RequestParam("id") int id, Model model){
        List<Reservation> reservations = reservationRepository.findAllByIdFlight(id);

        List<String> usernames = new ArrayList<>();
        List<Integer> seats = new ArrayList<>();
        List<String> seatsClass = new ArrayList<>();

        for(Reservation reservation : reservations){
            User user = userRepository.findUserById(reservation.getIdUser());
            usernames.add(user.getUsername());
            seats.add(reservation.getSeatNumber());
            seatsClass.add(reservation.getSeatClass());
        }

        model.addAttribute("users", usernames);
        model.addAttribute("seats", seats);
        model.addAttribute("seatsClass", seatsClass);

        return "/seeReservations";
    }
}

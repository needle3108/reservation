package com.database.tickets.reservation.controllers;

import com.database.tickets.reservation.models.User;
import com.database.tickets.reservation.models.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class UsersController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/index")
    public String home(HttpSession session){
        try{
            var username = session.getAttribute("username").toString();

            if(username.equals("admin")){
                return "/logAdmin";
            }

            else{
                return "/logUser";
            }
        }
        catch(NullPointerException e){
            return "/index";
        }

    }
    @GetMapping("/register")
    public String register(){
        return "/register";
    }

    @GetMapping("/login")
    public String login(){
        return "/login";
    }

    @PostMapping("/register/save")
    public String addUser(@RequestParam Map<String, String> newUser, HttpServletResponse response){
        System.out.println("ADD user");
        String newUsername = newUser.get("username");
        String newEmail = newUser.get("email");
        String newPassword = newUser.get("password");

        User candidate = userRepository.findByEmail(newEmail);

        if(candidate != null){
            response.setStatus(401);
            return "/register";
        }

        userRepository.save(new User(newUsername, newEmail, newPassword));
        response.setStatus(201);
        return "/addedUser";
    }

    @PostMapping("/login/authorization")
    public String loginAut(@RequestParam Map<String, String> logAttempt, HttpServletResponse response, HttpSession session){
        System.out.println("LOGIN attempt");
        String newEmail = logAttempt.get("email");
        String newPassword = logAttempt.get("password");

        User candidate = userRepository.findByEmailAndPassword(newEmail, newPassword);

        if(candidate == null){
            response.setStatus(401);
            return "/login";
        }

        if(candidate.getEmail().equals("admin")){
            session.setAttribute("username", candidate.getUsername());
            response.setStatus(201);
            return "/logAdmin";
        }

        session.setAttribute("username", candidate.getUsername());
        response.setStatus(201);
        return "/logUser";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "/logout";
    }
}

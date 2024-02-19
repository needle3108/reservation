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
        if(isUserLogged(session)){
            if(session.getAttribute("username").equals("admin")){
                return "/admin/logAdmin";
            }
            return "/user/logUser";
        }
        else{
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

    @GetMapping("/logout")
    public String logout(HttpSession session){
        if(!isUserLogged(session)){ return "/index"; }
        session.invalidate();
        return "/user/logout";
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
            return "/admin/logAdmin";
        }

        session.setAttribute("username", candidate.getUsername());
        response.setStatus(201);
        return "/user/logUser";
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

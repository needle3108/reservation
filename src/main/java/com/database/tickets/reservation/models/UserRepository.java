package com.database.tickets.reservation.models;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
    User findUserByUsername(String username);
    User findUserById(int id);
}

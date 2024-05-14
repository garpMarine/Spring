package com.adobe.orderapp.dao;

import com.adobe.orderapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface UserDao extends JpaRepository<User, Integer> {
    // Since email is unique, we'll find users by email
    Optional<User> findByEmail(String email);
}

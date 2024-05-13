package com.visa.shopapp.dao;

import java.util.Optional;

import com.visa.shopapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserDao extends JpaRepository<User, Integer> {
    // Since email is unique, we'll find users by email
    Optional<User> findByEmail(String email);
}

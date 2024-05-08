package com.adobe.orderapp.dao;

import com.adobe.orderapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerDao extends JpaRepository<Customer, String> {

    // select * from customers where fname = ?
    List<Customer> findByFirstName(String name);
    // select * from customers where fname = ? and lname = ?
    List<Customer> findByFirstNameAndLastName(String fname, String lname);
}

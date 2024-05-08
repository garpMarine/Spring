package com.adobe.orderapp.dao;

import com.adobe.orderapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Integer> {
}

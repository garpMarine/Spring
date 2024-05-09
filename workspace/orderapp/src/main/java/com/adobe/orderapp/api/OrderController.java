package com.adobe.orderapp.api;

import com.adobe.orderapp.entity.Order;
import com.adobe.orderapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {
    private  final OrderService service;

    @GetMapping()
    public List<Order> getOrders() {
        return service.getOrders();
    }

    // POST http://localhost:8080/api/orders
    @PostMapping()
    public String placeOrder(@RequestBody Order order) {
        return service.placeOrder(order);
    }
}

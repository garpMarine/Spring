package com.adobe.orderapp.service;

import com.adobe.orderapp.dto.User;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url="/users", accept = "application/json", contentType = "application/json")
public interface UserService {
    @GetExchange
    List<User> getUsers();
}

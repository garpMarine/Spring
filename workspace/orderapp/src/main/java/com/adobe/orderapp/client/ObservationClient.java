package com.adobe.orderapp.client;

import com.adobe.orderapp.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObservationClient implements CommandLineRunner {
    private final PostService service;
    @Override
    public void run(String... args) throws Exception {
        service.findAll(); // Observation
    }
}

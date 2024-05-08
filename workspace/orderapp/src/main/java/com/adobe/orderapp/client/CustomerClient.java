package com.adobe.orderapp.client;

import com.adobe.orderapp.entity.Customer;
import com.adobe.orderapp.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
public class CustomerClient implements CommandLineRunner {
    private final OrderService service; // instead of @Autowired use Constructor DI

    @Override
    public void run(String... args) throws Exception {
        insertCustomers();
    }

    private void insertCustomers() {
        if(service.getCustomersCount() == 0) {
            service.saveCustomer(Customer.builder().email("asha@adobe.com").firstName("Asha").lastName("Rao").build());
            service.saveCustomer(Customer.builder().email("harish@adobe.com").firstName("Harish").lastName("Sinha").build());
        }
    }
}

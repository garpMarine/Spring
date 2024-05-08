package com.adobe.orderapp.client;

import com.adobe.orderapp.entity.Product;
import com.adobe.orderapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
public class ProductClient implements CommandLineRunner {
    private final OrderService service; // instead of @Autowired use Constructor DI

    @Override
    public void run(String... args) throws Exception {
        insertProducts();
        getProducts();
    }

    private void getProducts() {
        List<Product> products = service.getProducts();
        for(Product p : products) {
            System.out.println(p); // toString() @Data
        }
    }

    private void insertProducts() {
        // id is auto increment
        if(service.getProductsCount() == 0) {
            service.addProduct(Product.builder().name("iPhone 15").quantity(100).price(89000.00).build());
            service.addProduct(Product.builder().name("Samsung TV").quantity(100).price(198000.00).build());
            service.addProduct(Product.builder().name("Wacom").quantity(100).price(4500.00).build());
            service.addProduct(Product.builder().name("LG AC").quantity(100).price(51000.00).build());
        }
    }


}

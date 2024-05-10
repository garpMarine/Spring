package com.adobe.orderapp.client;

import com.adobe.orderapp.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

//@Component
public class RestTemplateClient implements CommandLineRunner {
    @Autowired
    RestTemplate template;

    @Override
    public void run(String... args) throws Exception {
        getUsers();
        getProduct();
        addProduct();
        getAllProducts();
    }

    private void getAllProducts() {
        ResponseEntity<List<Product>> response = template.exchange("http://localhost:8080/api/products",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
        });

        List<Product> products = response.getBody();
        for(Product p: products) {
            System.out.println(p);
        }
    }


    private void addProduct() {
        Product p = Product.builder().name("Tata Play").price(4500.00).quantity(2000).build();
        ResponseEntity<Product> response = template.postForEntity("http://localhost:8080/api/products", p, Product.class);
        System.out.println(response.getStatusCode());
    }

    private void getUsers() {
        String result = template.getForObject("https://jsonplaceholder.typicode.com/users", String.class);
        System.out.println(result); // array of JSON
    }

    private void getProduct() {
        ResponseEntity<Product> response = template.getForEntity("http://localhost:8080/api/products/3", Product.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody()); // product
    }


}

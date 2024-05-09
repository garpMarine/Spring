package com.adobe.orderapp.api;

import com.adobe.orderapp.entity.Product;
import com.adobe.orderapp.exceptions.NotFoundException;
import com.adobe.orderapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {
    private final OrderService service;


    // http://localhost:8080/api/products
    // Query parameters
    //// http://localhost:8080/api/products?low=?&high=?
    @GetMapping()
    public List<Product> getProducts(@RequestParam(name="low", defaultValue = "0.0") double low,
                                     @RequestParam(name="high", defaultValue = "0.0") double high) {
        if(low == 0.0 && high == 0.0) {
            return service.getProducts();
        } else {
            return service.byRange(low, high);
        }
    }

    // http://localhost:8080/api/products/3
    // Path Parameter
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") int id) throws NotFoundException {
        return  service.getProductById(id);
    }

    // POST  // http://localhost:8080/api/products
    // payload contains JSON
    // @RequestBody JSON --> Java
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED) // 201
    public Product addProduct(@RequestBody Product p) {
        return service.addProduct(p);
    }

    // http://localhost:8080/api/products/2
    // payload contains the new product data
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable("id") int id, @RequestBody Product p) throws  NotFoundException{
        // todo update
        return service.getProductById(id);
    }

    // http://localhost:8080/api/products/2
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        // todo
        return "Product with ID " + id + " is deleted!!!";
    }
}

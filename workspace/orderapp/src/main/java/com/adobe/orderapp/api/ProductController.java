package com.adobe.orderapp.api;

import com.adobe.orderapp.entity.Product;
import com.adobe.orderapp.exceptions.NotFoundException;
import com.adobe.orderapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Cacheable(value="productCache", key="#id")
    @GetMapping("/cache/{id}")
    public Product getProductCache(@PathVariable("id") int id) throws NotFoundException {
        System.out.println("Cache Miss!!!");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) { }
        return  service.getProductById(id);
    }

    @GetMapping("/etag/{id}")
    public ResponseEntity<Product> getProductEtag(@PathVariable("id") int id) throws NotFoundException {
        Product p =  service.getProductById(id);
        return ResponseEntity.ok().eTag(Long.toString(p.hashCode())).body(p);
    }

    // POST  // http://localhost:8080/api/products
    // payload contains JSON
    // @RequestBody JSON --> Java
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED) // 201
    @Cacheable(value="productCache", key="#p.id", condition = "#p.price > 5000")
    public Product addProduct(@RequestBody @Valid Product p) {
        return service.addProduct(p);
    }

    // http://localhost:8080/api/products/2
    // payload contains the new product data
    @CachePut(value = "productCache", key="#id")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable("id") int id, @RequestBody Product p) throws  NotFoundException{
        // todo update
        return service.getProductById(id);
    }

    // http://localhost:8080/api/products/2
    @CacheEvict(value = "productCache", key="#id")
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        // todo
        return "Product with ID " + id + " is deleted!!!";
    }
}

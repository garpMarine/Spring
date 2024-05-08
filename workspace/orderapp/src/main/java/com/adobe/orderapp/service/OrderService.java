package com.adobe.orderapp.service;

import com.adobe.orderapp.dao.CustomerDao;
import com.adobe.orderapp.dao.ProductDao;
import com.adobe.orderapp.entity.Customer;
import com.adobe.orderapp.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductDao productDao; // constructor DI, no need for @Autowired
    private final CustomerDao customerDao;

    public List<Product> byRange(double low, double high) {
        return productDao.getByRange(low, high);
    }

    public long getProductsCount() {
        return productDao.count();
    }

    public long getCustomersCount() {
        return customerDao.count();
    }

    public Product addProduct(Product p) {
        return productDao.save(p);
    }

    public List<Product> getProducts() {
        return productDao.findAll();
    }

    public Product getProductById(int id) {
        Optional<Product> opt = productDao.findById(id);
        if(opt.isPresent()) {
            return opt.get();
        }
        return null; // throw exception
    }

    public void deleteById(int id) {
        productDao.deleteById(id);
    }

    public Customer saveCustomer(Customer c) {
        return customerDao.save(c);
    }

}

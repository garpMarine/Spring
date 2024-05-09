package com.adobe.orderapp.service;

import com.adobe.orderapp.aspect.Tx;
import com.adobe.orderapp.dao.CustomerDao;
import com.adobe.orderapp.dao.OrderDao;
import com.adobe.orderapp.dao.ProductDao;
import com.adobe.orderapp.entity.Customer;
import com.adobe.orderapp.entity.LineItem;
import com.adobe.orderapp.entity.Order;
import com.adobe.orderapp.entity.Product;
import jakarta.transaction.Transactional;
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
    private final OrderDao ordeDao;

    /*
        {
            "customer": {"email": "asha@adobe.com"},
            "items": [
                {"product": {"id": 3 }, "quantity" : 2},
                {"product": {"id": 4 }, "quantity" : 1}
            ]
        }
     */
    // atomic
    @Transactional
    public String placeOrder(Order order) {
        double total = 0.0;
        List<LineItem> items = order.getItems();
        for(LineItem item : items) {
            Product p = productDao.findById(item.getProduct().getId()).get();
            if(p.getQuantity() < item.getQuantity()) {
                throw  new IllegalArgumentException("Product not in Stock " + p.getName());
            }
            item.setAmount(p.getPrice() * item.getQuantity()); // TAX , Discount, ..
            p.setQuantity(p.getQuantity() - item.getQuantity()); // DIRTY Checking --> Update
            // no need for productDao.updateQty();
            total += item.getAmount();
        }
        order.setTotal(total);
        ordeDao.save(order); // cascade takes care of saving order and items
        return  "order placed!!!";
    }

    public List<Order> getOrders() {
        return  ordeDao.findAll();
    }

    public List<Product> byRange(double low, double high) {
        return productDao.getByRange(low, high);
    }

    @Tx
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

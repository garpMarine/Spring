package com.adobe.orderapp.api;

import com.adobe.orderapp.dao.ProductDao;
import com.adobe.orderapp.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@BasePathAwareController
public class SampleController {
    @Autowired
    ProductDao productDao;

    @RequestMapping(path = "hello", method = RequestMethod.GET)
    public @ResponseBody String getHello() {
        return  "Hello World!!!";
    }
    // override existing path
    @RequestMapping(path = "products", method = RequestMethod.GET)
    public @ResponseBody List<Product> getProducts() {
        return Arrays.asList(new Product(1,"a", 88, 11),
                new Product(2,"b", 861, 110));
    }
}

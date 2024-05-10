package com.adobe.orderapp;

import com.adobe.orderapp.api.ProductController;
import com.adobe.orderapp.entity.Product;
import com.adobe.orderapp.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

// static imports

import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // GET

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;


@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @MockBean
    OrderService service;

    @Autowired
    MockMvc mockMvc; // for CRUD operation, perform Http Verbs

    @Test
    public void getProductsTest() throws Exception {
        List<Product> products = Arrays.asList(
                Product.builder().id(1).name("A").price(6788).quantity(100).build(),
                Product.builder().id(1).name("B").price(8900).quantity(100).build()
                );
        //mocking
        when(service.getProducts()).thenReturn(products);
        // end mocking

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("A")));

        verify(service, times(1)).getProducts();
    }

    @Test
    public void addProductTest() throws Exception {

    }

    @Test
    public void addProductExceptionTest() throws Exception {
        Product p = new Product(0, "", -1500.00, -100);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(p); // json to be sent as payload
        // no need to Mock service
        mockMvc.perform(post("/api/products")
                .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem("Name is required")));

        verifyNoInteractions(service);
    }
}

package com.adobe.orderapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name="products")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private int id;
    @NotBlank(message = "Name is required")
    private String name;
    @Min(value = 10, message = "Price ${validatedValue}  should be more than {value}")
    private double price;
    @Min(value = 1, message = "Quantity ${validatedValue}  should be more than {value}")
    private int quantity;
}

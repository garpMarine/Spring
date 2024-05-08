package com.adobe.orderapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private int id;
    private String name;
    private double price;
    private int quantity;
}

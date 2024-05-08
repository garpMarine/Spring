package com.adobe.orderapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="customers")
@Data

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    private String email;

    @Column(name="FNAME", length = 100)
    private String firstName;

    @Column(name="LNAME", length = 100)
    private String lastName;
}

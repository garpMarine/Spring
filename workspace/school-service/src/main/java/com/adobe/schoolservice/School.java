package com.adobe.schoolservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int schoolId;
    private String name;
}

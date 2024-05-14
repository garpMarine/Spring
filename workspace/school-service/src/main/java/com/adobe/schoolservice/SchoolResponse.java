package com.adobe.schoolservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolResponse {
    private String name; // name of School
    List<Student> students; // thro microservice
}

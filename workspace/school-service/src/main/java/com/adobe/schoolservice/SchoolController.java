package com.adobe.schoolservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService service;

    @PostMapping
    public School save(@RequestBody School s) {
        return  service.save(s);
    }

    @GetMapping
    public List<School>  getAll() {
        return service.getSchools();
    }

    @GetMapping("/with-students/{school-id}")
    public SchoolResponse find(@PathVariable("school-id") int id) {
        return  service.getSchoolAndStudents(id);
    }
}

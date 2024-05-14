package com.example.studentservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
public class StudentController {
    private final StudentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student save(@RequestBody Student s) {
        return service.saveStudent(s);
    }

    @GetMapping
    public List<Student> getStudents() {
        return service.findAllStudents();
    }

    @GetMapping("/school/{school-id}")
    public List<Student> getStudentsOfSchool(@PathVariable("school-id") int id) {
        return service.findAllStudentsbySchool(id);
    }
}

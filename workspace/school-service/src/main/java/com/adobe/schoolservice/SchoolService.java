package com.adobe.schoolservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolDao schoolDao;
    private final StudentClient client; //feign

    public School save(School s) {
        return schoolDao.save(s);
    }

    public List<School> getSchools() {
        return schoolDao.findAll();
    }

    // micro service
    public SchoolResponse getSchoolAndStudents(Integer id) {
        var school = schoolDao.findById(id).get();
        var students = client.findAllStudentsBySchool(id); // ms

        return SchoolResponse.builder()
        .name(school.getName()).
                students(students)
                .build();
    }
}

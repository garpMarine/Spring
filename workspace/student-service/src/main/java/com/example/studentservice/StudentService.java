package com.example.studentservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentDao studentDao;

    public Student saveStudent(Student s) {
        return studentDao.save(s);
    }

    public List<Student> findAllStudents() {
        return studentDao.findAll();
    }

    public List<Student> findAllStudentsbySchool(Integer sid) {
        return studentDao.findAllBySchoolId(sid);
    }
}

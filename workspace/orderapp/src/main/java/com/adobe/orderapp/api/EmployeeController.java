package com.adobe.orderapp.api;

import com.adobe.orderapp.dto.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {
    Employee employee = new Employee();

    public EmployeeController() {
        employee.setId(124);
        employee.setTitle("Sr. Software Engineer");
        var personal = new HashMap<String, String>();
        personal.put("firstName", "Harry");
        personal.put("lastName", "Potter");
        personal.put("phone", "1234567890");
        employee.setPersonal(personal);

        var skills = new ArrayList<String>();
        skills.add("Spring Boot");
        skills.add("React");
        employee.setSkills(skills);
    }

    // PATCH
    // http://localhost:8080/api/employees/124
    @PatchMapping(path = "/{id}" , consumes = "application/json-patch+json")
    public Employee updateEmployee(@PathVariable("id") int id, @RequestBody JsonPatch patch) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        var jsonNode = patch.apply(mapper.readTree(mapper.writeValueAsString(employee)));
        System.out.println(jsonNode);
        return mapper.treeToValue(jsonNode, Employee.class);
    }

}

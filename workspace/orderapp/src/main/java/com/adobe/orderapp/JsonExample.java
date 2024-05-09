package com.adobe.orderapp;

import com.adobe.orderapp.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;

public class JsonExample {
    public static void main(String[] args) throws Exception{
        String employee  = """
            {
                "title": "Sr.Software Engineer",
                "personal" : {
                    "firstName": "Harry",
                    "lastName" : "Potter",
                    "phone" : "1234567890"
                },
                "skills": [
                    "Spring Boot",
                    "React"
                ]
            }
            """;

        // payload sent by client
        var patch = """
                [
                    {"op": "replace", "path": "/title", "value" : "Team Lead"},
                    {"op" : "remove", "path": "/personal/phone" },
                    {"op": "add", "path" : "/personal/email", "value": "harry@adobe.com"},
                    {"op" : "add" , "path" : "/skills/1" , "value" : "AWS"}
                ]
                """;

        ObjectMapper mapper = new ObjectMapper();
//        Product p = Product.builder().name("iPhone 15").quantity(100).price(89000.00).build();
//        System.out.println(mapper.writeValueAsString(p));
        JsonPatch jsonPatch = JsonPatch.fromJson(mapper.readTree(patch));
        var targetEmp = jsonPatch.apply(mapper.readTree(employee));
        System.out.println(targetEmp);
    }
}

/*

{"title":"Team Lead",
"personal":{"firstName":"Harry","lastName":"Potter","email":"harry@adobe.com"},
"skills":["Spring Boot","AWS","React"]}
 */
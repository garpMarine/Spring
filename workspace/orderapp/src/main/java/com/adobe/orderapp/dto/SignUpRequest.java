package com.adobe.orderapp.dto;


import com.adobe.orderapp.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Builder.Default
    private Role role = Role.ROLE_ADMIN;
}


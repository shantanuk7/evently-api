package com.springproject.eventmanagementsystem.dto;

import com.springproject.eventmanagementsystem.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthRegistrationResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
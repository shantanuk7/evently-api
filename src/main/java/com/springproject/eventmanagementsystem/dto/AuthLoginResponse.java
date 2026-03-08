package com.springproject.eventmanagementsystem.dto;

import com.springproject.eventmanagementsystem.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginResponse {
    private String token;
    private Long userId;
    private String email;
    private Role role;
}
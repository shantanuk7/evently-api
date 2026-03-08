package com.springproject.eventmanagementsystem.controller;

import com.springproject.eventmanagementsystem.config.SecurityConfig;
import com.springproject.eventmanagementsystem.dto.AuthLoginRequest;
import com.springproject.eventmanagementsystem.dto.AuthLoginResponse;
import com.springproject.eventmanagementsystem.dto.AuthRegistrationRequest;
import com.springproject.eventmanagementsystem.dto.AuthRegistrationResponse;
import com.springproject.eventmanagementsystem.model.Role;
import com.springproject.eventmanagementsystem.repository.UserRepository;
import com.springproject.eventmanagementsystem.service.AuthService;
import com.springproject.eventmanagementsystem.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    @Test
    void shouldReturnCreatedUserDetails_whenUserIsSuccessfullyRegistered() throws Exception {
        AuthRegistrationRequest request = new AuthRegistrationRequest("Rahul", "rahul.attendee@gmail.com", "Test@123");
        AuthRegistrationResponse mockResponse = new AuthRegistrationResponse(1L, "Rahul", "rahul.attendee@gmail.com", Role.ATTENDEE);

        when(authService.registerUser(any(AuthRegistrationRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Rahul"))
                .andExpect(jsonPath("$.email").value("rahul.attendee@gmail.com"))
                .andExpect(jsonPath("$.role").value("ATTENDEE"));
    }

    @Test
    void shouldReturnTokenAndUserDetails_whenUserIsSuccessfullyLoggedIn() throws Exception {
        AuthLoginRequest request = new AuthLoginRequest("rahul.attendee@gmail.com", "Test@123");
        AuthLoginResponse mockResponse = new AuthLoginResponse("token", 1L, "rahul.attendee@gmail.com", Role.ATTENDEE);

        when(authService.login(any(AuthLoginRequest.class))).thenReturn(mockResponse);

        ResultActions results = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("token"))
                .andExpect(jsonPath("$.data.userId").value(1L))
                .andExpect(jsonPath("$.data.email").value("rahul.attendee@gmail.com"))
                .andExpect(jsonPath("$.data.role").value("ATTENDEE"));
    }
}
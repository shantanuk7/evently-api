package com.springproject.eventmanagementsystem.controller;

import com.springproject.eventmanagementsystem.config.SecurityConfig;
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
}
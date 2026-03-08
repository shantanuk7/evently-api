package com.springproject.eventmanagementsystem.controller;

import com.springproject.eventmanagementsystem.dto.AuthRegistrationRequest;
import com.springproject.eventmanagementsystem.dto.AuthRegistrationResponse;
import com.springproject.eventmanagementsystem.model.Role;
import com.springproject.eventmanagementsystem.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// AuthControllerTest.java
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void shouldReturnCreatedUserDetails_whenUserIsSuccessfullyRegistered() {
        AuthRegistrationRequest request = new AuthRegistrationRequest("Rahul", "rahul.attendee@gmail.com", "Test@123");
        AuthRegistrationResponse mockResponse = new AuthRegistrationResponse(1L, "Rahul", "rahul.attendee@gmail.com", Role.ATTENDEE);

        when(authService.registerUser(request)).thenReturn(mockResponse);

        ResponseEntity<AuthRegistrationResponse> result = authController.register(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(mockResponse, result.getBody());
        verify(authService, times(1)).registerUser(request);
    }
}
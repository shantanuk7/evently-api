package com.springproject.eventmanagementsystem.service;

import com.springproject.eventmanagementsystem.dto.AuthRegistrationRequest;
import com.springproject.eventmanagementsystem.dto.AuthRegistrationResponse;
import com.springproject.eventmanagementsystem.model.Role;
import com.springproject.eventmanagementsystem.model.UserEntity;
import com.springproject.eventmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        UserEntity mockAttendee = new UserEntity();
        mockAttendee.setId(1L);
        mockAttendee.setName("Rahul");
        mockAttendee.setEmail("rahul.attendee@gmail.com");
        mockAttendee.setPassword("Test@123");
        mockAttendee.setRole(Role.ATTENDEE);
    }

    @Test
    void shouldSaveCorrectUserDetails_WhenNewUserIsRegistered() {

        // Given
        AuthRegistrationRequest request =
                new AuthRegistrationRequest("Rahul", "rahul.attendee@gmail.com", "Test@123");

        when(passwordEncoder.encode("Test@123")).thenReturn("encodedPassword");

        UserEntity savedUser = new UserEntity();
        savedUser.setId(1L);
        savedUser.setName("Rahul");
        savedUser.setEmail("rahul.attendee@gmail.com");
        savedUser.setRole(Role.ATTENDEE);

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        // When
        authService.registerUser(request);

        // Then
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());

        UserEntity capturedUser = userCaptor.getValue();

        assertEquals("Rahul", capturedUser.getName());
        assertEquals("rahul.attendee@gmail.com", capturedUser.getEmail());
        assertEquals("encodedPassword", capturedUser.getPassword());
        assertEquals(Role.ATTENDEE, capturedUser.getRole());
    }
}
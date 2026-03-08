package com.springproject.eventmanagementsystem.service;

import com.springproject.eventmanagementsystem.dto.AuthLoginRequest;
import com.springproject.eventmanagementsystem.dto.AuthLoginResponse;
import com.springproject.eventmanagementsystem.dto.AuthRegistrationRequest;
import com.springproject.eventmanagementsystem.dto.AuthRegistrationResponse;
import com.springproject.eventmanagementsystem.exception.ConflictException;
import com.springproject.eventmanagementsystem.model.Role;
import com.springproject.eventmanagementsystem.model.UserEntity;
import com.springproject.eventmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @Mock
    AuthenticationManager authenticationManager;

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

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
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

    @Test
    void shouldThrowConflictException_WhenUserWithSameEmailAlreadyExists() {
        AuthRegistrationRequest request =
                new AuthRegistrationRequest("Rahul", "rahul.attendee@gmail.com", "Test@123");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new UserEntity()));

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> authService.registerUser(request)
        );

        assertEquals("CONFLICT_ERROR", exception.getCode());
        assertEquals("User with email:" + request.getEmail() + " already exists", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));

    }

    @Test
    void shouldLoginSuccessfully_AndReturnToken_WhenUserSubmitsValidCredentials() {
        // Given
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("rahul.attendee@gmail.com");
        user.setPassword("Test@123");
        user.setRole(Role.ATTENDEE);

        AuthLoginRequest request = new AuthLoginRequest(user.getEmail(), user.getPassword());

        when(userRepository.findByEmail(eq(request.getEmail()))).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token");

        // When
        AuthLoginResponse response = authService.login(request);

        // Then
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(eq(request.getEmail()));
        verify(jwtService).generateToken(eq(user));
        assertEquals("token", response.getToken());
        assertEquals(user.getId(), response.getUserId());
        assertEquals(Role.ATTENDEE, response.getRole());
        assertEquals(request.getEmail(), response.getEmail());
    }
}
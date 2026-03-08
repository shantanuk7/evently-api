package com.springproject.eventmanagementsystem.service;

import com.springproject.eventmanagementsystem.dto.AuthLoginRequest;
import com.springproject.eventmanagementsystem.dto.AuthLoginResponse;
import com.springproject.eventmanagementsystem.dto.AuthRegistrationRequest;
import com.springproject.eventmanagementsystem.dto.AuthRegistrationResponse;
import com.springproject.eventmanagementsystem.exception.ConflictException;
import com.springproject.eventmanagementsystem.exception.ResourceNotFoundException;
import com.springproject.eventmanagementsystem.model.Role;
import com.springproject.eventmanagementsystem.model.UserEntity;
import com.springproject.eventmanagementsystem.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthRegistrationResponse registerUser(AuthRegistrationRequest request){
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        Optional<UserEntity> user = userRepository.findByEmail(normalizedEmail);
        if (user.isPresent()) {
            throw new ConflictException("CONFLICT_ERROR", "User with email:"+request.getEmail()+" already exists");
        }

        UserEntity newUser = new UserEntity();
        newUser.setName(request.getName());
        newUser.setEmail(normalizedEmail);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(Role.ATTENDEE);

        UserEntity savedUser = userRepository.save(newUser);

        return new AuthRegistrationResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public AuthLoginResponse login(AuthLoginRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        normalizedEmail,
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND","user not found with email "+ normalizedEmail));

        String token = jwtService.generateToken(user);

        return new AuthLoginResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}

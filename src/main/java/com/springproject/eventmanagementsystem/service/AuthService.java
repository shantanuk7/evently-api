package com.springproject.eventmanagementsystem.service;

import com.springproject.eventmanagementsystem.dto.AuthRegistrationRequest;
import com.springproject.eventmanagementsystem.dto.AuthRegistrationResponse;
import com.springproject.eventmanagementsystem.exception.ConflictException;
import com.springproject.eventmanagementsystem.model.Role;
import com.springproject.eventmanagementsystem.model.UserEntity;
import com.springproject.eventmanagementsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthRegistrationResponse registerUser(AuthRegistrationRequest request){
        Optional<UserEntity> user = userRepository.findByEmail(request.getEmail().toLowerCase().trim());
        if (user.isPresent()) {
            throw new ConflictException("CONFLICT_ERROR", "User with email:"+request.getEmail()+" already exists");
        }

        UserEntity newUser = new UserEntity();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail().toLowerCase());
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
}

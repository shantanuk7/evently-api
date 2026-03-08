package com.springproject.eventmanagementsystem.service;

import com.springproject.eventmanagementsystem.dto.AuthRegistrationRequest;
import com.springproject.eventmanagementsystem.dto.AuthRegistrationResponse;
import com.springproject.eventmanagementsystem.model.Role;
import com.springproject.eventmanagementsystem.model.UserEntity;
import com.springproject.eventmanagementsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ATTENDEE);

        UserEntity savedUser = userRepository.save(user);

        return new AuthRegistrationResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}

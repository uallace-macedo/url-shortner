package com.java_api.service;

import com.java_api.controller.dto.user.UserRegisterRequest;
import com.java_api.controller.dto.user.UserLoginRequest;
import com.java_api.exception.custom.UserEmailAlreadyTakenException;
import com.java_api.exception.custom.WrongCredentialsException;
import com.java_api.model.User;
import com.java_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(UserRegisterRequest userRegisterRequest) {
        Optional<User> exists = userRepository.findByEmail(userRegisterRequest.email());
        if (exists.isPresent()) {
            throw new UserEmailAlreadyTakenException("Email (" + userRegisterRequest.email() + ") already taken!");
        }

        String hashedPassword = passwordEncoder.encode(userRegisterRequest.password());
        User user = new User();
        user.setName(userRegisterRequest.name());
        user.setEmail(userRegisterRequest.email());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public User login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByEmail(userLoginRequest.email())
                .orElseThrow(() -> new WrongCredentialsException("Invalid credentials"));

        if(!passwordEncoder.matches(userLoginRequest.password(), user.getPassword())) {
            throw new WrongCredentialsException("Invalid credentials");
        }

        return user;
    }
}

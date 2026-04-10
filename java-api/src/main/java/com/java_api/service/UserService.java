package com.java_api.service;

import com.java_api.dto.user.UserCreateRequest;
import com.java_api.dto.user.UserDTO;
import com.java_api.exception.custom.UserEmailAlreadyTaken;
import com.java_api.mapper.UserMapper;
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

    public UserDTO create(UserCreateRequest userCreateRequest) {
        Optional<User> exists = userRepository.findByEmail(userCreateRequest.email());
        if (exists.isPresent()) {
            throw new UserEmailAlreadyTaken("Email (" + userCreateRequest.email() + ") already taken!");
        }

        String hashedPassword = passwordEncoder.encode(userCreateRequest.password());
        User user = new User();
        user.setName(userCreateRequest.name());
        user.setEmail(userCreateRequest.email());
        user.setPassword(hashedPassword);

        User userSaved = userRepository.save(user);
        return UserMapper.toDTO(userSaved);
    }
}

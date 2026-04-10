package com.java_api.controller;

import com.java_api.controller.dto.user.UserCreateRequest;
import com.java_api.controller.dto.user.UserDTO;
import com.java_api.controller.mapper.UserMapper;
import com.java_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDTO create(@Valid @RequestBody UserCreateRequest userData) {
        return UserMapper.toDTO(userService.create(userData));
    }
}

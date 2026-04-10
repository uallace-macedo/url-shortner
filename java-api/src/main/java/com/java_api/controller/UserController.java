package com.java_api.controller;

import com.java_api.dto.user.UserCreateRequest;
import com.java_api.dto.user.UserDTO;
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
        return userService.create(userData);
    }
}

package com.java_api.mapper;

import com.java_api.dto.user.UserDTO;
import com.java_api.model.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    public static User toModel(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.id());
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setCreatedAt(userDTO.createdAt());

        return user;
    }
}

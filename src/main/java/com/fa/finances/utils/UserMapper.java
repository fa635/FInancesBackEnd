package com.fa.finances.utils;


import com.fa.finances.dto.UserDTO;
import com.fa.finances.models.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}


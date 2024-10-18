package com.nisum.users.service;

import com.nisum.users.domain.User;
import com.nisum.users.domain.dto.UserDto;
import com.nisum.users.model.UserListRs;
import com.nisum.users.model.UserRq;
import com.nisum.users.model.UserRs;

import java.util.UUID;

public interface UserService {

    /**
     * Create a new User with their phones.
     */
    UserRs createUser(UserRq userRq);

    /**
     * Update existing User .
     */
    UserRs updateUser(UUID id,UserRq userRq);

    /**
     * Update existing User .
     */
    UserRs updateUser(UserDto userDto);

    /**
     * Get all Users.
     */
    UserListRs getAllUsers();

    /**
     * Get Users by email.
     */
    UserDto getUserEmail(String email);
    /**
     * Get UserRs.
     */
    UserRs getUserId(UUID id);

    /**
     * Disable a  User by id UUID.
     */
    void disableUser(UUID id);

    /**
     * Enable a  User by id UUID.
     */
    void enableUser(UUID id);

    /**
     * Get User.
     */
    User getUser(UUID id);

}

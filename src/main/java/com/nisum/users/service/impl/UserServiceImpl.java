package com.nisum.users.service.impl;

import com.nisum.users.config.security.JWTUtil;
import com.nisum.users.domain.User;
import com.nisum.users.domain.dto.UserDto;
import com.nisum.users.mapper.UserMapper;
import com.nisum.users.model.UserListRs;
import com.nisum.users.model.UserRq;
import com.nisum.users.model.UserRs;
import com.nisum.users.repository.UserRepository;
import com.nisum.users.service.UserService;
import com.nisum.users.util.PasswordValidator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final JWTUtil jwtUtil;

    private final PasswordValidator passwordValidator;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, JWTUtil jwtUtil, PasswordValidator passwordValidator) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public UserRs createUser(UserRq userRq) {

        passwordValidator.validatePassword(userRq.getPassword());

        User user = userMapper.toEntity(userRq);
        user.setToken(generateToken(userRq));
        user.setEmail(userRq.getEmail().toLowerCase());
        user.setLastLogin(LocalDateTime.now());
        setPhones(user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserRs getUserId(UUID id) {
        return userMapper.toDto(getUser(id));
    }

    @Override
    public UserDto getUserEmail(String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email.toLowerCase());
        return optionalUser.map(userMapper::toDtoUser)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + email));
    }

    @Override
    public UserListRs getAllUsers() {
        UserListRs userListRs = new UserListRs();
        userListRs.setUserList(userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList()));
        return userListRs;
    }

    @Override
    public UserRs updateUser(UUID id, UserRq userRq) {

        passwordValidator.validatePassword(userRq.getPassword());
        User  existUser = getUser(id);
        existEmail(existUser.getEmail(),userRq.getEmail());
        userMapper.updateEntityFromDto(userRq, existUser);
        setPhones(existUser);
        existUser.setEmail(existUser.getEmail().toLowerCase());
        return userMapper.toDto(userRepository.save(existUser));
    }

    @Override
    public UserRs updateUser(UserDto userDto) {

        passwordValidator.validatePassword(userDto.getPassword());
        User  user = userMapper.toEntity(userDto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void disableUser(UUID id) {
        User user = getUser(id);
        user.setIsActive(Boolean.FALSE);
        userRepository.save(user);
    }

    @Override
    public void enableUser(UUID id) {
        User user = getUser(id);
        user.setIsActive(Boolean.TRUE);
        userRepository.save(user);
    }

    @Override
    public User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    private void setPhones(User userPhone){

        if(userPhone.getPhones()!=null)
            userPhone.getPhones().forEach(phone -> phone.setUser(userPhone));
    }

    private String generateToken(UserRq userRq) {
        try {
            UserDto userDto = userMapper.toDto(userRq);
            return jwtUtil.generateToken(userDto);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred generating the token");
        }
    }

    private void existEmail(String oldEmail, String newEmail) {
        if (!oldEmail.equalsIgnoreCase(newEmail)) {
            throw new UnsupportedOperationException("Email cannot be updated.");
        }
    }
}

package com.nisum.users.service.impl;

import com.nisum.users.config.security.JWTUtil;
import com.nisum.users.domain.Phone;
import com.nisum.users.domain.User;
import com.nisum.users.domain.dto.UserDto;
import com.nisum.users.mapper.UserMapper;
import com.nisum.users.model.UserListRs;
import com.nisum.users.model.UserRq;
import com.nisum.users.model.UserRs;
import com.nisum.users.repository.UserRepository;
import com.nisum.users.util.PasswordValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Mock
    PasswordValidator passwordValidator;

    @Mock
    JWTUtil jwtUtil;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    private UserRq userRq;

    @BeforeEach
    void setUp() {
        userRq = new UserRq();
        userRq.setName("Juan nieves");
        userRq.setEmail("juan@gmail.com");
        userRq.setPassword("Hunter26*");
        userRq.setPhones(new ArrayList<>());
    }

    @Test
    void createUserTest() throws IOException {

        Mockito.doNothing().when(passwordValidator).validatePassword(Mockito.any());
        Mockito.when(userMapper.toEntity(Mockito.any(UserRq.class))).thenReturn(new User());
        Mockito.when(userMapper.toDto(Mockito.any(UserRq.class))).thenReturn(new UserDto());
        Mockito.when(jwtUtil.generateToken(Mockito.any())).thenReturn("token");
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(new User());
        Mockito.when(userMapper.toDto(Mockito.any(User.class))).thenReturn(new UserRs());
        UserRs userRs = userServiceImpl.createUser(userRq);

        Assertions.assertNotNull(userRs);

        Mockito.verify(userMapper).toEntity(Mockito.any(UserRq.class));
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void createUserAndGenerateTokenFailTest() throws IOException {

        Mockito.doNothing().when(passwordValidator).validatePassword(Mockito.any());
        Mockito.when(userMapper.toEntity(Mockito.any(UserRq.class))).thenReturn(new User());
        Mockito.when(userMapper.toDto(Mockito.any(UserRq.class))).thenReturn(new UserDto());
        Mockito.when(jwtUtil.generateToken(Mockito.any())).thenThrow(new IOException());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userServiceImpl.createUser(userRq);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("An error occurred generating the token",exception.getMessage());

        Mockito.verify(userMapper).toEntity(Mockito.any(UserRq.class));
    }

    @Test
    void getUserIdTest() {

        Mockito.when(userMapper.toDto(Mockito.any(User.class))).thenReturn(new UserRs());
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(new User()));
        UserRs userRs = userServiceImpl.getUserId(UUID.randomUUID());

        Assertions.assertNotNull(userRs);

        Mockito.verify(userMapper).toDto(Mockito.any(User.class));
        Mockito.verify(userRepository).findById(Mockito.any());
    }

    @Test
    void getUserEmailTest() {

        Mockito.when(userMapper.toDtoUser(Mockito.any(User.class))).thenReturn(new UserDto());
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(new User()));
        UserDto userDto = userServiceImpl.getUserEmail("juan@gmail.com");

        Assertions.assertNotNull(userDto);

        Mockito.verify(userMapper).toDtoUser(Mockito.any(User.class));
        Mockito.verify(userRepository).findByEmail(Mockito.any());
    }

    @Test
    void getAllUserTest() {

        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<>());
        UserListRs userRsList = userServiceImpl.getAllUsers();

        Assertions.assertNotNull(userRsList);

        Mockito.verify(userRepository).findAll();
    }

    @Test
    void updateUserTest() {
        Phone phone = new Phone();
        phone.setCityCode("1");
        List<Phone> phones = new ArrayList<>();
        phones.add(phone);
        User user = new User();
        user.setEmail("juan@gmail.com");
        user.setPhones(phones);

        Mockito.doNothing().when(passwordValidator).validatePassword(Mockito.any());
        Mockito.doNothing().when(userMapper).updateEntityFromDto(Mockito.any(UserRq.class), Mockito.any());
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(new User());
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(userMapper.toDto(Mockito.any(User.class))).thenReturn(new UserRs());
        UserRs userRs = userServiceImpl.updateUser(UUID.randomUUID(), userRq);

        Assertions.assertNotNull(userRs);

        Mockito.verify(userMapper).updateEntityFromDto(Mockito.any(UserRq.class), Mockito.any());
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void disableUserTest() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(new User());
        userServiceImpl.disableUser(UUID.randomUUID());

        Mockito.verify(userRepository).findById(Mockito.any());
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void enableUserTest() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(new User()));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(new User());
        userServiceImpl.enableUser(UUID.randomUUID());

        Mockito.verify(userRepository).findById(Mockito.any());
        Mockito.verify(userRepository).save(Mockito.any());
    }


}

package com.nisum.users.service.impl;

import com.nisum.users.config.security.JWTUtil;
import com.nisum.users.domain.dto.UserDto;
import com.nisum.users.mapper.UserMapper;
import com.nisum.users.model.LoginRq;
import com.nisum.users.model.LoginRs;
import com.nisum.users.model.UserRs;
import com.nisum.users.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    AuthServiceImpl authServiceImpl;

    @Mock
    JWTUtil jwtUtil;

    @Mock
    UserMapper userMapper;

    @Mock
    UserService userService;

    private LoginRq loginRq;

    @BeforeEach
    void setUp() {
        loginRq = new LoginRq();
        loginRq.setEmail("juan@gmail.com");
        loginRq.setPassword("Hunter26*");

        ReflectionTestUtils.setField(authServiceImpl, "TOKEN_MINUTES", "30");
    }

    @Test
    void loginUserTest() throws IOException {

        UserDto userDto = new UserDto();
        userDto.setEmail("juan@gmail.com");
        userDto.setPassword("Hunter26*");

        Mockito.when(userService.getUserEmail(Mockito.any())).thenReturn(userDto);
        Mockito.when(jwtUtil.generateToken(Mockito.any())).thenReturn("token");
        Mockito.when(userService.updateUser(Mockito.any())).thenReturn(new UserRs());
        LoginRs loginRs = authServiceImpl.loginUser(loginRq);

        Assertions.assertNotNull(loginRs);

        Mockito.verify(userService).getUserEmail(Mockito.any());
        Mockito.verify(jwtUtil).generateToken(Mockito.any());
    }

    @Test
    void loginUserAndGenerateTokenFailTest() throws IOException {

        UserDto userDto = new UserDto();
        userDto.setEmail("juan@gmail.com");
        userDto.setPassword("Hunter26*");
        Mockito.when(userService.getUserEmail(Mockito.any())).thenReturn(userDto);
        Mockito.when(jwtUtil.generateToken(Mockito.any())).thenThrow(new IOException());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            authServiceImpl.loginUser(loginRq);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("An error occurred generating the token",exception.getMessage());
        Mockito.verify(userService).getUserEmail(Mockito.any());
    }

}

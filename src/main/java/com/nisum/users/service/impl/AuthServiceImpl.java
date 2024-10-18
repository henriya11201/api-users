package com.nisum.users.service.impl;

import com.nisum.users.config.security.JWTUtil;
import com.nisum.users.domain.User;
import com.nisum.users.domain.dto.UserDto;
import com.nisum.users.mapper.UserMapper;
import com.nisum.users.model.LoginRq;
import com.nisum.users.model.LoginRs;
import com.nisum.users.model.UserRq;
import com.nisum.users.model.UserRs;
import com.nisum.users.service.AuthService;
import com.nisum.users.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${user.jwt.token-expires-minutes}")
    private String TOKEN_MINUTES;

    @Value("${user.jwt.token-type-auth}")
    private String TYPE_AUTH;

    private final UserMapper userMapper;
    private final JWTUtil jwtUtil;
    private final UserService service;
    private static final BigDecimal SECONDS_IN_A_MINUTE = BigDecimal.valueOf(60);
    private UserDto userDto;

    public AuthServiceImpl(UserMapper userMapper, JWTUtil jwtUtil, UserService service) {

        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.service = service;


    }

    @Override
    public LoginRs loginUser(LoginRq loginRq) {
        LoginRs loginRs = new LoginRs();
        try {
                validateUser(loginRq);
                String token = jwtUtil.generateToken(userDto);
                userDto.setToken(token);
                userDto.setLastLogin(LocalDateTime.now());
                service.updateUser(userDto);

                loginRs.setToken(token);
                loginRs.setExpiresIn(convertMinutesToSeconds(TOKEN_MINUTES));
                loginRs.setTokenType(TYPE_AUTH);


        } catch (IOException e) {
            throw new RuntimeException("An error occurred generating the token");
        }
    return loginRs;
    }

    private void validateUser(LoginRq loginRq){
        String email = loginRq.getEmail();
         this.userDto = service.getUserEmail(email);

        if (!loginRq.getEmail().equalsIgnoreCase(userDto.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect User.");

        if (!loginRq.getPassword().equals(userDto.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Password.");
    }


    private BigDecimal convertMinutesToSeconds(String minutesTokenStr) {
        try {
            long minutesAsLong = Long.parseLong(minutesTokenStr);
            BigDecimal minutes = BigDecimal.valueOf(minutesAsLong);
            return minutes.multiply(BigDecimal.valueOf(60));

        } catch (NumberFormatException e) {
            throw new RuntimeException("Incorrect Format.");
        }
    }

}

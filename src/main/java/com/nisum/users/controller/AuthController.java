package com.nisum.users.controller;

import com.nisum.users.api.AuthApi;
import com.nisum.users.model.LoginRq;
import com.nisum.users.model.LoginRs;
import com.nisum.users.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController implements AuthApi {

    @Autowired
    private AuthService service;

    public ResponseEntity<LoginRs> loginUser(LoginRq loginRq){
        return new ResponseEntity<>(service.loginUser(loginRq), HttpStatus.OK);

    }
}

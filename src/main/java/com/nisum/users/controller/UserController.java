package com.nisum.users.controller;

import com.nisum.users.api.UserApi;
import com.nisum.users.model.UserListRs;
import com.nisum.users.model.UserRq;
import com.nisum.users.model.UserRs;
import com.nisum.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController implements UserApi {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<UserRs> createUser(UserRq userRq) {
        return new ResponseEntity<>(service.createUser(userRq), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserListRs> getUser() {
        return new ResponseEntity<>(service.getAllUsers(), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<UserRs> getUserId(UUID id) {
        return new ResponseEntity<>(service.getUserId(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserRs> updateUser(UUID id,UserRq userRq) {
        return new ResponseEntity<>(service.updateUser(id, userRq), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> disableUser(UUID id) {
        service.disableUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> enableUser(UUID id) {
        service.enableUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

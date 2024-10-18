package com.nisum.users.service;

import com.nisum.users.model.LoginRq;
import com.nisum.users.model.LoginRs;
import com.nisum.users.model.UserRq;
import com.nisum.users.model.UserRs;

public interface AuthService {

    /**
     * Generate a Token JWT.
     */
    LoginRs loginUser(LoginRq loginRq);
}

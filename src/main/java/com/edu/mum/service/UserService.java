package com.edu.mum.service;

import com.edu.mum.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmailAndPassword(String email, String pass);
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User save(User user);
}

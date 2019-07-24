package com.edu.mum.service.impl;

import com.edu.mum.domain.User;
import com.edu.mum.repository.RoleRepository;
import com.edu.mum.repository.UserRepository;
import com.edu.mum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;

    private static final String USER_ROLE = "ROLE_USER";

    @Autowired
    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmailAndPassword(String email, String pass){
        return userRepository.findByEmailAndPassword(email,pass);
    }
    @Override
    public Optional<User> findByUsername(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        // Encode plaintext password
        user.setPassword(user.getPassword());
        user.setActive(1);
        // Set Role to ROLE_USER
        user.setRoles(roleRepository.findByRole(USER_ROLE));
        return userRepository.saveAndFlush(user);
    }
}

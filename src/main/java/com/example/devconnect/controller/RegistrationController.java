package com.example.devconnect.controller;

import com.example.devconnect.model.UserAccount;
import com.example.devconnect.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register/user")
    public UserAccount registerUser(@RequestBody UserAccount user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        return userAccountRepository.save(user);
    }
}

package com.example.devconnect.controller;

import com.example.devconnect.model.UserAccount;
import com.example.devconnect.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserAccountController {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountController(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register/user")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserAccount());
        return "register";
    }

    @PostMapping("/register/user")
    public String registerUser(@ModelAttribute UserAccount user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userAccountRepository.save(user);
        return "redirect:/login";
    }
}

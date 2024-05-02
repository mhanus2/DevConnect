package com.example.devconnect.controller;

import com.example.devconnect.model.UserAccount;
import com.example.devconnect.repository.UserAccountRepository;
import com.example.devconnect.service.UserAccountDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
public class UserAccountController {

    private final UserAccountDetailsService userAccountDetailsService;
    private final PasswordEncoder passwordEncoder;

    public UserAccountController(UserAccountDetailsService userAccountDetailsService, PasswordEncoder passwordEncoder) {
        this.userAccountDetailsService = userAccountDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserAccount());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserAccount user) {
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userAccountDetailsService.saveUser(user);
        return "redirect:/profile/" + user.getId();
    }

    @GetMapping("/profile/{id}")
    public String getProfileById(@PathVariable Integer id, Model model) {
        Optional<UserAccount> user = userAccountDetailsService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("profile", user);
            return "profile";
        } else {
            return "error";
        }
    }

    @GetMapping("profile/edit/{id}")
    public String showEditUserForm(@PathVariable Integer id, Model model, Principal principal) {
        Optional<UserAccount> user = userAccountDetailsService.getUserById(id);
        if (user.isPresent()) {
            UserAccount currentUser = user.get();
            if (!currentUser.getUsername().equals(principal.getName())) {
                return "error";
            }
            model.addAttribute("userAccount", currentUser);
            return "editProfile";
        } else {
            return "error";
        }
    }

    @PostMapping("profile/save")
    public String saveUser(@ModelAttribute UserAccount user, Principal principal) {
        Optional<UserAccount> existingUser = userAccountDetailsService.getUserById(user.getId());
        if (existingUser.isPresent()) {
            UserAccount currentUser = existingUser.get();
            if (!currentUser.getUsername().equals(principal.getName())) {
                return "error"; // or redirect to an error page
            }
            user.setUsername(currentUser.getUsername());
            user.setPassword(currentUser.getPassword());
            user.setRole(currentUser.getRole());
            userAccountDetailsService.saveUser(user);
            return "redirect:/profile/" + user.getId();
        } else {
            return "error";
        }
    }


}

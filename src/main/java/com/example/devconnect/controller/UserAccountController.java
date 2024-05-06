package com.example.devconnect.controller;

import com.example.devconnect.model.Skill;
import com.example.devconnect.model.Tag;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.SkillService;
import com.example.devconnect.service.TagService;
import com.example.devconnect.service.UserAccountDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;
import java.util.List;


@Controller
public class UserAccountController {

    private final UserAccountDetailsService userAccountDetailsService;
    private final SkillService skillService;
    private final PasswordEncoder passwordEncoder;

    public UserAccountController(UserAccountDetailsService userAccountDetailsService, SkillService skillService, PasswordEncoder passwordEncoder) {
        this.userAccountDetailsService = userAccountDetailsService;
        this.skillService = skillService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserAccount());
        return "userAccount/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserAccount user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userAccountDetailsService.saveUser(user);
        return "redirect:/login";
    }


    @GetMapping("/profile/{id}")
    public String getProfileById(@PathVariable Integer id, Model model) {
        Optional<UserAccount> user = userAccountDetailsService.getUserById(id);
        if (user.isPresent()) {
            List<Skill> skills = skillService.getAllSkills(user.get());
            model.addAttribute("profile", user);
            model.addAttribute("skills", skills);
            return "userAccount/profile";
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
            return "userAccount/editProfile";
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

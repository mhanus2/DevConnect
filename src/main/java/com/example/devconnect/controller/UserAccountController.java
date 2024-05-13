package com.example.devconnect.controller;

import com.example.devconnect.model.Project;
import com.example.devconnect.model.Skill;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.ProjectService;
import com.example.devconnect.service.SkillService;
import com.example.devconnect.service.UserAccountDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
public class UserAccountController {
    private final UserAccountDetailsService userAccountDetailsService;
    private final SkillService skillService;
    private final ProjectService projectService;
    private final PasswordEncoder passwordEncoder;

    public UserAccountController(UserAccountDetailsService userAccountDetailsService, SkillService skillService, ProjectService projectService, PasswordEncoder passwordEncoder) {
        this.userAccountDetailsService = userAccountDetailsService;
        this.skillService = skillService;
        this.projectService = projectService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "userAccount/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserAccount());
        return "userAccount/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserAccount user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/register";
        }

        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userAccountDetailsService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/profiles/{id}")
    public String getProfile(@PathVariable Integer id, Model model, Principal principal) {
        Optional<UserAccount> user = userAccountDetailsService.getUserById(id);
        if (user.isPresent()) {
            if (principal != null) {
                Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
                loggedUser.ifPresent(userAccount -> model.addAttribute("isAdmin", userAccount.isAdmin()));
            }
            List<Skill> skills = skillService.getAllSkills(user.get());
            model.addAttribute("profile", user);
            model.addAttribute("userId", user.get().getId());
            model.addAttribute("skills", skills);
            return "userAccount/profile";
        } else {
            return "error/404";
        }
    }

    @GetMapping("profiles/edit/{id}")
    public String showEditUserForm(@PathVariable Integer id, Model model, Principal principal) {
        if (principal != null) {
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            Optional<UserAccount> user = userAccountDetailsService.getUserById(id);

            if (user.isPresent() && loggedUser.isPresent()) {
                UserAccount currentUser = user.get();
                if (currentUser.getUsername().equals(loggedUser.get().getUsername()) || loggedUser.get().isAdmin()) {
                    user.ifPresent(userAccount -> model.addAttribute("userId", userAccount.getId()));
                    model.addAttribute("userAccount", currentUser);
                    return "userAccount/editProfile";
                } else {
                    return "error/403";
                }
            } else {
                return "error/404";
            }
        }
        return "error/401";
    }

    @PostMapping("profiles/edit/{id}")
    public String saveUser(@Valid @ModelAttribute UserAccount user, Principal principal, BindingResult bindingResult) {
        if (principal != null) {
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            Optional<UserAccount> existingUser = userAccountDetailsService.getUserById(user.getId());

            if (existingUser.isPresent() && loggedUser.isPresent()) {
                UserAccount currentUser = existingUser.get();
                if (currentUser.getUsername().equals(loggedUser.get().getUsername()) || loggedUser.get().isAdmin()) {
                    if (bindingResult.hasErrors()) {
                        return "redirect:/projects/edit/" + user.getId();
                    }
                    user.setUsername(currentUser.getUsername());
                    user.setPassword(currentUser.getPassword());
                    user.setRole(currentUser.getRole());
                    userAccountDetailsService.saveUser(user);
                    return "redirect:/profiles/" + user.getId();
                } else {
                    return "error/403";
                }
            } else {
                return "error/404";
            }
        }
        return "error/401";
    }

    @GetMapping("/profiles/{userId}/projects")
    public String getHome(@PathVariable Integer userId, Model model, Principal principal) {
        Optional<UserAccount> user = userAccountDetailsService.getUserById(userId);
        if (user.isPresent()) {
            List<Project> projects = projectService.getProjectsByOwner(user.get());
            model.addAttribute("projects", projects);
            if (principal != null) {
                Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
                if (loggedUser.isPresent()) {
                    model.addAttribute("isAdmin", loggedUser.get().isAdmin());
                    model.addAttribute("userId", loggedUser.get().getId());
                }
            }
            return "project/projects";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/profiles/delete/{id}")
    public String deleteUser(@PathVariable Integer id, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        if (principal != null) {
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            Optional<UserAccount> user = userAccountDetailsService.getUserById(id);
            if (user.isPresent() && loggedUser.isPresent()) {
                UserAccount currentUser = user.get();
                if (currentUser.getUsername().equals(loggedUser.get().getUsername()) || loggedUser.get().isAdmin()) {
                    new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
                    userAccountDetailsService.deleteUser(currentUser.getId());
                    return "redirect:/";
                } else {
                    return "error/403";
                }
            } else {
                return "error/404";
            }
        }
        return "error/401";
    }
}

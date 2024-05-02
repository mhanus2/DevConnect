package com.example.devconnect.controller;

import com.example.devconnect.model.Project;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.ProjectService;
import com.example.devconnect.service.UserAccountDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final ProjectService projectService;
    private final UserAccountDetailsService userAccountDetailsService;

    public HomeController(ProjectService projectService, UserAccountDetailsService userAccountDetailsService) {
        this.projectService = projectService;
        this.userAccountDetailsService = userAccountDetailsService;
    }

    @GetMapping("/")
    public String getHome(Model model, Principal principal) {
        model.addAttribute("currentPage", "home");
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        if (principal != null) {
            String username = principal.getName();
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(username);
//            user.ifPresent(userAccount -> model.addAttribute("userId", "/profile/" + userAccount.getId()));
            user.ifPresent(userAccount -> model.addAttribute("userId", userAccount.getId()));
        }
        return "home";
    }
}

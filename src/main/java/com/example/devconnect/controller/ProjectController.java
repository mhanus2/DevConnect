package com.example.devconnect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectController {

    @GetMapping("/projects")
    public String getProjects() {
        return null;
    }

    @GetMapping("/admin")
    public String handleAdmin() {
        return "admin";
    }

    @GetMapping("/user")
    public String handleUser() {
        return "user";
    }

    @GetMapping("/")
    public String handleWelcome() {
        return "hello";
    }
}

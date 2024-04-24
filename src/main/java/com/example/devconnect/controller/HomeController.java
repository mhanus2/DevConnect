package com.example.devconnect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        return "home";
    }
}

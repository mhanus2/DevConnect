package com.example.devconnect.controller;

import com.example.devconnect.model.Project;
import com.example.devconnect.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProjectController {

    ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("project/{id}")
    public String getProjectById(@PathVariable Integer id, Model model) {
        Project project = projectService.getProjectById(id);
        if (project != null) {
            model.addAttribute("project", project);
            return "project";
        } else {
            return "error";
        }
    }
}

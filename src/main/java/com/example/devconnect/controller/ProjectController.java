package com.example.devconnect.controller;

import com.example.devconnect.model.Project;
import com.example.devconnect.model.Tag;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.ProjectService;
import com.example.devconnect.service.TagService;
import com.example.devconnect.service.UserAccountDetailsService;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class ProjectController {

    private final UserAccountDetailsService userAccountDetailsService;
    ProjectService projectService;
    private final TagService tagService;


    public ProjectController(ProjectService projectService, UserAccountDetailsService userAccountDetailsService, TagService tagService) {
        this.projectService = projectService;
        this.userAccountDetailsService = userAccountDetailsService;
        this.tagService = tagService;
    }

    @GetMapping("/users/{userId}/projects")
    public String getHome(@PathVariable Integer userId, Model model) {
        UserAccount user = userAccountDetailsService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Project> projects = projectService.getProjectsByOwner(user);
        model.addAttribute("projects", projects);
        return "project/projects";
    }

    @GetMapping("/project/{id}")
    public String getProjectById(@PathVariable Integer id, Model model) {
        Project project = projectService.getProjectById(id);
        if (project != null) {
            List<Tag> tags = project.getTags();
            model.addAttribute("project", project);
            model.addAttribute("tags", tags);
            return "project/project";
        } else {
            return "error";
        }
    }

    @GetMapping("/project/create")
    public String showCreateProjectForm(Model model) {
        model.addAttribute("project", new Project());
        List<Tag> tags = tagService.getAllTags();
        model.addAttribute("tags", tags);
        return "project/createProject";
    }

    @PostMapping("/project/save")
    public String saveProject(@ModelAttribute Project project, Principal principal) {
        System.out.println(project.getTags());
        Optional<UserAccount> optionalOwner = userAccountDetailsService.getUserByUsername(principal.getName());
        if (optionalOwner.isPresent()) {
            UserAccount owner = optionalOwner.get();
            project.setOwner(owner);
            projectService.saveProject(project);
            Integer projectId = project.getId();
            return String.format("redirect:/project/%s", projectId);
        } else {
            return "error";
        }
    }

    @GetMapping("/project/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, Principal principal) {
        Project project = projectService.getProjectById(id);
        UserAccount owner = project.getOwner();

        if (Objects.equals(owner.getUsername(), principal.getName())) {
            model.addAttribute("project", project);
            List<Tag> tags = tagService.getAllTags();
            model.addAttribute("tags", tags);
            return "project/editProject";
        } else {
            return "error";
        }
    }

    @PostMapping("/project/edit")
    public String editProject(@ModelAttribute Project project, Principal principal) {
        Project existingProject = projectService.getProjectById(project.getId());

        UserAccount owner = existingProject.getOwner();

        if (Objects.equals(owner.getUsername(), principal.getName())) {
            project.setOwner(owner);
            projectService.editProject(project);
            return "redirect:/project/" + project.getId();
        } else {
            return "error";
        }
    }

    @GetMapping("/project/delete/{id}")
    public String deleteProject(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes) {
        Project project = projectService.getProjectById(id);
        UserAccount owner = project.getOwner();
        if (Objects.equals(owner.getUsername(), principal.getName())) {
            projectService.deleteProject(id);
            redirectAttributes.addFlashAttribute("successMessage", "Project deleted successfully");
            return "redirect:/";
        } else {
            return "error";
        }
    }
}

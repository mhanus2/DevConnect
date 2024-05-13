package com.example.devconnect.controller;

import com.example.devconnect.model.*;
import com.example.devconnect.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class ProjectController {
    private final UserAccountDetailsService userAccountDetailsService;
    private final ProjectService projectService;
    private final TagService tagService;
    private final CommentService commentService;
    private final ImageService imageService;

    public ProjectController(ProjectService projectService, UserAccountDetailsService userAccountDetailsService, TagService tagService, CommentService commentService, ImageService imageService) {
        this.projectService = projectService;
        this.userAccountDetailsService = userAccountDetailsService;
        this.tagService = tagService;
        this.commentService = commentService;
        this.imageService = imageService;
    }

    @GetMapping("/")
    public String getHome(Model model, Principal principal) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects.reversed());
        if (principal != null) {
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());
            user.ifPresent(userAccount -> model.addAttribute("userId", userAccount.getId()));
            user.ifPresent(userAccount -> model.addAttribute("isAdmin", userAccount.isAdmin()));
        }
        return "home";
    }

    @GetMapping("/projects/{id}")
    public String getProjectById(@PathVariable Integer id, Model model, Principal principal) {
        Project project = projectService.getProjectById(id);
        if (project != null) {
            List<Tag> tags = project.getTags();
            List<Comment> comments = commentService.getAllComments(project);
            List<Image> images = imageService.getAllImages(project);
            if (principal != null) {
                Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());
                user.ifPresent(userAccount -> model.addAttribute("userId", userAccount.getId()));
                user.ifPresent(userAccount -> model.addAttribute("isAdmin", userAccount.isAdmin()));
            }
            model.addAttribute("project", project);
            model.addAttribute("tags", tags);
            model.addAttribute("comments", comments);
            model.addAttribute("images", images);
            return "project/project";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/projects/create")
    public String showCreateProjectForm(Model model, Principal principal) {
        if (principal != null) {
            List<Tag> tags = tagService.getAllTags();
            model.addAttribute("project", new Project());
            model.addAttribute("tags", tags);
            model.addAttribute("edit", false);
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());
            user.ifPresent(userAccount -> model.addAttribute("userId", userAccount.getId()));
            user.ifPresent(userAccount -> model.addAttribute("isAdmin", userAccount.isAdmin()));
            return "project/form";
        }
        return "error/401";
    }

    @PostMapping("/projects/create")
    public String saveProject(@Valid @ModelAttribute Project project, BindingResult bindingResult, Principal principal) {
        if (principal != null) {
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());
            if (user.isPresent()) {
                if (bindingResult.hasErrors()) {
                    return "redirect:/projects/create";
                }

                project.setOwner(user.get());
                projectService.saveProject(project);
                return "redirect:/projects/" + project.getId();
            }
        }
        return "error/401";
    }

    @GetMapping("/projects/edit/{id}")
    public String showEditForm(@Valid @PathVariable("id") Integer id, Model model, Principal principal) {
        if (principal != null) {
            Project project = projectService.getProjectById(id);
            UserAccount owner = project.getOwner();
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());

            if (user.isPresent()) {
                if (Objects.equals(owner.getUsername(), user.get().getUsername()) || user.get().isAdmin()) {
                    List<Tag> tags = tagService.getAllTags();
                    model.addAttribute("project", project);
                    model.addAttribute("tags", tags);
                    model.addAttribute("userId", user.get().getId());
                    model.addAttribute("isAdmin", user.get().isAdmin());
                    model.addAttribute("edit", true);
                    return "project/form";
                } else {
                    return "error/403";
                }
            }
        }
        return "error/401";
    }

    @PostMapping("/projects/edit")
    public String editProject(@Valid @ModelAttribute Project project, BindingResult bindingResult, Principal principal) {
        if (principal != null) {
            Project existingProject = projectService.getProjectById(project.getId());
            UserAccount owner = existingProject.getOwner();
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());

            if (user.isPresent()) {
                if (Objects.equals(owner.getUsername(), principal.getName()) || user.get().isAdmin()) {
                    if (bindingResult.hasErrors()) {
                        return "redirect:/projects/edit/" + project.getId();
                    }

                    project.setOwner(owner);
                    projectService.editProject(project);
                    return "redirect:/projects/" + project.getId();
                } else {
                    return "error/403";
                }
            }
        }
        return "error/401";
    }

    @GetMapping("/projects/delete/{id}")
    public String deleteProject(@PathVariable Integer id, Principal principal) {
        if (principal != null) {
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());
            Project project = projectService.getProjectById(id);
            UserAccount owner = project.getOwner();

            if (user.isPresent()) {
                if (Objects.equals(owner.getUsername(), principal.getName()) || user.get().isAdmin()) {
                    for (Image image : imageService.getAllImages(project)) {
                        imageService.delete(image.getId());
                    }
                    for (Comment comment : commentService.getAllComments(project)) {
                        commentService.deleteComment(comment.getId());
                    }
                    projectService.deleteProject(id);
                    return "redirect:/";
                } else {
                    return "error/403";
                }
            }
        }
        return "error/401";
    }
}

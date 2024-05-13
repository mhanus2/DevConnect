package com.example.devconnect.controller;

import com.example.devconnect.model.*;
import com.example.devconnect.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            String username = principal.getName();
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(username);
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
                String username = principal.getName();
                Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(username);
                user.ifPresent(userAccount -> model.addAttribute("isAdmin", userAccount.isAdmin()));
                model.addAttribute("userId", user.get().getId());
                model.addAttribute("isAdmin", user.get().isAdmin());
            }
            model.addAttribute("project", project);
            model.addAttribute("tags", tags);
            model.addAttribute("comments", comments);
            model.addAttribute("images", images);
            return "project/project";
        } else {
            return "error";
        }
    }

    @GetMapping("/projects/create")
    public String showCreateProjectForm(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("project", new Project());
            List<Tag> tags = tagService.getAllTags();
            model.addAttribute("tags", tags);
            model.addAttribute("userId", userAccountDetailsService.getUserByUsername(principal.getName()).get().getId());
            model.addAttribute("isAdmin", userAccountDetailsService.getUserByUsername(principal.getName()).get().isAdmin());
            return "project/createProject";
        }
        return "error";
    }

    @PostMapping("/projects/create")
    public String saveProject(@ModelAttribute Project project, Principal principal) {
        if (principal != null) {
            Optional<UserAccount> optionalOwner = userAccountDetailsService.getUserByUsername(principal.getName());
            if (optionalOwner.isPresent()) {
                UserAccount owner = optionalOwner.get();
                project.setOwner(owner);
                projectService.saveProject(project);
                Integer projectId = project.getId();
                return "redirect:/projects/" + projectId;
            }
        }
        return "error";
    }

    @GetMapping("/projects/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, Principal principal) {
        if (principal != null) {
            Project project = projectService.getProjectById(id);
            UserAccount owner = project.getOwner();
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());

            if (Objects.equals(owner.getUsername(), principal.getName()) || user.get().isAdmin()) {
                model.addAttribute("project", project);
                List<Tag> tags = tagService.getAllTags();
                model.addAttribute("tags", tags);
                model.addAttribute("userId", user.get().getId());
                model.addAttribute("isAdmin", user.get().isAdmin());
                return "project/editProject";
            }
        }
        return "error";
    }

    @PostMapping("/projects/edit")
    public String editProject(@ModelAttribute Project project, Principal principal) {
        if (principal != null) {
            Project existingProject = projectService.getProjectById(project.getId());
            UserAccount owner = existingProject.getOwner();
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());

            if (Objects.equals(owner.getUsername(), principal.getName()) || user.get().isAdmin()) {
                project.setOwner(owner);
                projectService.editProject(project);
                return "redirect:/projects/" + project.getId();
            }
        }
        return "error";
    }

    @GetMapping("/projects/delete/{id}")
    public String deleteProject(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal != null) {
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());

            Project project = projectService.getProjectById(id);
            UserAccount owner = project.getOwner();
            if (Objects.equals(owner.getUsername(), principal.getName()) || user.get().isAdmin()) {
                for (Image image : imageService.getAllImages(project)) {
                    imageService.delete(image.getId());
                }
                for (Comment comment : commentService.getAllComments(project)) {
                    commentService.deleteComment(comment.getId());
                }
                projectService.deleteProject(id);
                redirectAttributes.addFlashAttribute("successMessage", "Pojekt úspěšně odstraněn");
                return "redirect:/";
            }
        }
        return "error";
    }
}

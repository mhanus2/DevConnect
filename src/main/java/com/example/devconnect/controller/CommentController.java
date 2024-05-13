package com.example.devconnect.controller;

import com.example.devconnect.model.Comment;
import com.example.devconnect.model.Project;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.CommentService;
import com.example.devconnect.service.ProjectService;
import com.example.devconnect.service.UserAccountDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final UserAccountDetailsService userAccountDetailsService;
    private final ProjectService projectService;

    public CommentController(CommentService commentService, UserAccountDetailsService userAccountDetailsService, ProjectService projectService) {
        this.commentService = commentService;
        this.userAccountDetailsService = userAccountDetailsService;
        this.projectService = projectService;
    }

    @GetMapping("/projects/{id}/comments/create")
    public String createComment(Model model, @PathVariable Integer id, Principal principal) {
        if (principal != null) {
            model.addAttribute("comment", new Comment());
            model.addAttribute("projectId", id);
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());
            user.ifPresent(userAccount -> model.addAttribute("userId", userAccount.getId()));
            user.ifPresent(userAccount -> model.addAttribute("isAdmin", userAccount.isAdmin()));
            return "comment/create";
        }
        return "error";
    }

    @PostMapping("/projects/{id}/comments/create")
    public String createComment(@ModelAttribute Comment comment, Principal principal, @PathVariable Integer id) {
        if (principal != null) {
            Optional<UserAccount> userAccount = userAccountDetailsService.getUserByUsername(principal.getName());
            Project project = projectService.getProjectById(id);

            if (userAccount.isPresent()) {
                UserAccount owner = userAccount.get();
                commentService.createComment(id, owner.getId(), comment.getValue());
                return "redirect:/projects/" + project.getId();
            }
        }
        return "error";
    }

    @GetMapping("/projects/{projectId}/comments/delete/{commentId}")
    public String deleteProject(@PathVariable Integer projectId, @PathVariable Integer commentId, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal != null) {
            Comment comment = commentService.getCommentById(commentId);
            UserAccount owner = comment.getOwner();
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            if (Objects.equals(owner.getUsername(), principal.getName()) || loggedUser.get().isAdmin()) {
                commentService.deleteComment(commentId);
                redirectAttributes.addFlashAttribute("successMessage", "Komentář úspěšně odstraněn");
                return "redirect:/projects/" + projectId;
            }
        }
        return "error";
    }
}

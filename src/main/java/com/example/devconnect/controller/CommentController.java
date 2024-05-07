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
//@RequestMapping("/comments")
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
    public String createComment(Model model, @PathVariable Integer id) {
        model.addAttribute("comment", new Comment());
        model.addAttribute("projectId", id);
        return "comment/create";
    }

    @PostMapping("/projects/{id}/comments/create")
    public String createComment(@ModelAttribute Comment comment, Principal principal, @PathVariable Integer id) {
        Optional<UserAccount> userAccount = userAccountDetailsService.getUserByUsername(principal.getName());
        Project project = projectService.getProjectById(id);
        if (userAccount.isPresent()) {
            UserAccount owner = userAccount.get();
            comment.setOwner(owner);
            comment.setProject(project);
            commentService.createComment(comment);
            return "redirect:/project/" + project.getId();
        } else {
            return "error";
        }
    }

    @GetMapping("/projects/{projectId}/comments/delete/{commentId}")
    public String deleteProject(@PathVariable Integer projectId, @PathVariable Integer commentId, Principal principal, RedirectAttributes redirectAttributes) {
        Comment comment = commentService.getCommentById(commentId);
        UserAccount owner = comment.getOwner();
        if (Objects.equals(owner.getUsername(), principal.getName())) {
            commentService.deleteComment(commentId);
            redirectAttributes.addFlashAttribute("successMessage", "Comment deleted successfully");
            return "redirect:/project/" + projectId;
        } else {
            return "error";
        }
    }

    // todo - admin can delete comments too

}

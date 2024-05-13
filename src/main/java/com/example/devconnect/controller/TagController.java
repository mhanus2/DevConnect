package com.example.devconnect.controller;

import com.example.devconnect.model.Tag;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.TagService;
import com.example.devconnect.service.UserAccountDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final UserAccountDetailsService userAccountDetailsService;

    public TagController(TagService tagService, UserAccountDetailsService userAccountDetailsService) {
        this.tagService = tagService;
        this.userAccountDetailsService = userAccountDetailsService;
    }

    @GetMapping("")
    public String showTags(Model model, Principal principal) {
        if (principal != null) {
            UserAccount loggedUser = userAccountDetailsService.getUserByUsername(principal.getName()).get();
            if (loggedUser.isAdmin()) {
                List<Tag> tags = tagService.getAllTags();
                model.addAttribute("tags", tags);
                model.addAttribute("userId", loggedUser.getId());
                model.addAttribute("isAdmin", loggedUser.isAdmin());
                model.addAttribute("popularTags", tagService.getPopularTags());
                return "tag/tags";
            }
        }
        return "error";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, Principal principal) {
        if (principal != null) {
            UserAccount loggedUser = userAccountDetailsService.getUserByUsername(principal.getName()).get();
            if (loggedUser.isAdmin()) {
                model.addAttribute("tag", new Tag());
                model.addAttribute("isAdmin", loggedUser.isAdmin());
                model.addAttribute("userId", loggedUser.getId());
                return "tag/createTag";
            }
        }
        return "error";
    }

    @PostMapping("/create")
    public String createTag(@ModelAttribute Tag tag, Principal principal) {
        if (principal != null) {
            if (userAccountDetailsService.getUserByUsername(principal.getName()).get().isAdmin()) {
                tagService.createSkill(tag);
                return "redirect:/tags";
            }
        }
        return "error";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, Principal principal, @PathVariable Integer id) {
        if (principal != null) {
            UserAccount loggedUser = userAccountDetailsService.getUserByUsername(principal.getName()).get();
            if (loggedUser.isAdmin()) {
                model.addAttribute("tag", tagService.getTag(id));
                model.addAttribute("isAdmin", loggedUser.isAdmin());
                model.addAttribute("userId", loggedUser.getId());
                return "tag/editTag";
            }
        }
        return "error";
    }

    @PostMapping("/edit/{id}")
    public String showEditForm(@ModelAttribute Tag tag, Principal principal) {
        if (principal != null) {
            if (userAccountDetailsService.getUserByUsername(principal.getName()).get().isAdmin()) {
                tagService.updateTag(tag);
                return "redirect:/tags";
            }
        }
        return "error";
    }

    @GetMapping("/delete/{id}")
    public String deleteTag(Principal principal, @PathVariable Integer id) {
        if (principal != null) {
            if (userAccountDetailsService.getUserByUsername(principal.getName()).get().isAdmin()) {
                tagService.delete(id);
                return "redirect:/tags";
            }
        }
        return "error";
    }
}

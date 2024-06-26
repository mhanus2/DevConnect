package com.example.devconnect.controller;

import com.example.devconnect.model.Tag;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.TagService;
import com.example.devconnect.service.UserAccountDetailsService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            if (loggedUser.isPresent()) {
                if (loggedUser.get().isAdmin()) {
                    List<Tag> tags = tagService.getAllTags();
                    model.addAttribute("tags", tags);
                    model.addAttribute("userId", loggedUser.get().getId());
                    model.addAttribute("isAdmin", loggedUser.get().isAdmin());
                    model.addAttribute("popularTags", tagService.getPopularTags());
                    return "tag/tags";
                } else {
                    return "error/403";
                }
            }
        }
        return "error/401";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, Principal principal) {
        if (principal != null) {
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            if (loggedUser.isPresent()) {
                if (loggedUser.get().isAdmin()) {
                    model.addAttribute("tag", new Tag());
                    model.addAttribute("isAdmin", loggedUser.get().isAdmin());
                    model.addAttribute("userId", loggedUser.get().getId());
                    model.addAttribute("edit", false);
                    return "tag/form";
                } else {
                    return "error/403";
                }
            }
        }
        return "error/401";
    }

    @PostMapping("/create")
    public String createTag(@Valid @ModelAttribute Tag tag, BindingResult bindingResult, Principal principal) {
        if (principal != null) {
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            if (loggedUser.isPresent()) {
                if (loggedUser.get().isAdmin()) {
                    if (bindingResult.hasErrors()) {
                        return "redirect:/create";
                    }

                    tagService.createSkill(tag);
                    return "redirect:/tags";
                } else {
                    return "error/403";
                }
            }
        }
        return "error/401";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, Principal principal, @PathVariable Integer id) {
        if (principal != null) {
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            if (loggedUser.isPresent()) {
                if (loggedUser.get().isAdmin()) {
                    Tag tag = tagService.getTag(id);
                    if (tag != null) {
                        model.addAttribute("tag", tag);
                        model.addAttribute("isAdmin", loggedUser.get().isAdmin());
                        model.addAttribute("userId", loggedUser.get().getId());
                        model.addAttribute("edit", true);
                        return "tag/form";
                    } else {
                        return "error/404";
                    }
                } else {
                    return "error/403";
                }
            }
        }
        return "error/401";
    }

    @PostMapping("/edit/{id}")
    public String showEditForm(@Valid @ModelAttribute Tag tag, BindingResult bindingResult, Principal principal) {
        if (principal != null) {
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            if (loggedUser.isPresent()) {
                if (loggedUser.get().isAdmin()) {
                    if (bindingResult.hasErrors()) {
                        return "redirect:/edit/" + tag.getId();
                    }
                    tagService.updateTag(tag);
                    return "redirect:/tags";
                } else {
                    return "error/403";
                }
            }
        }
        return "error/401";
    }

    @GetMapping("/delete/{id}")
    public String deleteTag(Principal principal, @PathVariable Integer id) {
        if (principal != null) {
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            if (loggedUser.isPresent()) {
                if (loggedUser.get().isAdmin()) {
                    Tag tag = tagService.getTag(id);
                    if (tag != null) {
                        tagService.delete(id);
                        return "redirect:/tags";
                    } else {
                        return "error/404";
                    }
                } else {
                    return "error/403";
                }
            }
        }
        return "error/401";
    }
}

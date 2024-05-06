package com.example.devconnect.controller;

import com.example.devconnect.model.Skill;
import com.example.devconnect.model.Tag;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.SkillService;
import com.example.devconnect.service.TagService;
import com.example.devconnect.service.UserAccountDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/tags")
public class TagController {

    private TagService tagService;
    private UserAccountDetailsService userAccountDetailsService;

    public TagController(TagService tagService, UserAccountDetailsService userAccountDetailsService) {
        this.tagService = tagService;
        this.userAccountDetailsService = userAccountDetailsService;
    }

    @GetMapping("/{id}")
    public String showSkill(@PathVariable Integer id, Model model) {
        Tag tag = tagService.getTag(id);
        model.addAttribute("tag", tag);
        return "tag/tag";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("tag", new Tag());
        return "tag/createTag";
    }

    @PostMapping("/create")
    public String createTag(@ModelAttribute Tag tag) {
        // TODO - only admin
        tagService.createSkill(tag);
        return "redirect:/tags/" + tag.getId();
    }
}

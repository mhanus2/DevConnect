package com.example.devconnect.controller;

import com.example.devconnect.model.Skill;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.SkillService;
import com.example.devconnect.service.UserAccountDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/skills")
public class SkillController {

    private SkillService skillService;
    private UserAccountDetailsService userAccountDetailsService;

    public SkillController(SkillService skillService, UserAccountDetailsService userAccountDetailsService) {
        this.skillService = skillService;
        this.userAccountDetailsService = userAccountDetailsService;
    }

    @GetMapping("/{id}")
    public String showSkill(@PathVariable Integer id, Model model) {
        Skill skill = skillService.getSkill(id);
        model.addAttribute("skill", skill);
        return "skill/skill";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("skill", new Skill());
        return "skill/createSkill";
    }

    @PostMapping("/create")
    public String createSkill(@ModelAttribute Skill skill, Principal principal) {
        Optional<UserAccount> optionalOwner = userAccountDetailsService.getUserByUsername(principal.getName());
        if (optionalOwner.isPresent()) {
            UserAccount owner = optionalOwner.get();
            skill.setOwner(owner);
            skillService.createSkill(skill);
            return "redirect:/skills/" + skill.getId();
        } else {
            return "error";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, Principal principal) {
        Skill skill = skillService.getSkill(id);
        UserAccount owner = skill.getOwner();

        if (Objects.equals(owner.getUsername(), principal.getName())) {
            model.addAttribute("skill", skill);
            return "skill/editSkill";
        } else {
            return "error";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateSkill(@PathVariable Integer id, @ModelAttribute Skill skill, Principal principal) {
        Skill existingSkill = skillService.getSkill(skill.getId());

        UserAccount owner = existingSkill.getOwner();

        if (Objects.equals(owner.getUsername(), principal.getName())) {
            skill.setOwner(owner);
            skillService.updateSkill(skill);
            return "redirect:/skills/" + skill.getId();
        } else {
            return "error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteSkill(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes) {
        Skill skill = skillService.getSkill(id);
        UserAccount owner = skill.getOwner();
        if (Objects.equals(owner.getUsername(), principal.getName())) {
            skillService.deleteSkill(id);
            redirectAttributes.addFlashAttribute("successMessage", "Skill deleted successfully");
            return "redirect:/profile/" + owner.getId();
        } else {
            return "error";
        }
    }
}

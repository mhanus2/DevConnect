package com.example.devconnect.controller;

import com.example.devconnect.model.Skill;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.SkillService;
import com.example.devconnect.service.UserAccountDetailsService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Controller
public class SkillController {
    private final SkillService skillService;
    private final UserAccountDetailsService userAccountDetailsService;

    public SkillController(SkillService skillService, UserAccountDetailsService userAccountDetailsService) {
        this.skillService = skillService;
        this.userAccountDetailsService = userAccountDetailsService;
    }

    @GetMapping("profiles/{profileId}/skills/create")
    public String showCreateForm(Model model, Principal principal, @PathVariable String profileId) {
        if (principal != null) {
            model.addAttribute("skill", new Skill());
            model.addAttribute("userId", profileId);
            model.addAttribute("isAdmin", userAccountDetailsService.getUserByUsername(principal.getName()).get().isAdmin());
            model.addAttribute("edit", false);
            return "skill/form";
        }
        return "error";
    }

    @PostMapping("profiles/{profileId}/skills/create")
    public String createSkill(@Valid @ModelAttribute Skill skill, BindingResult bindingResult, Principal principal, @PathVariable Integer profileId) {
        if (principal != null) {
            Optional<UserAccount> optionalOwner = userAccountDetailsService.getUserById(profileId);
            Optional<UserAccount> loggedUser = userAccountDetailsService.getUserByUsername(principal.getName());
            if (optionalOwner.isPresent()) {
                if (loggedUser.get() == optionalOwner.get() || loggedUser.get().isAdmin()) {
                    if (bindingResult.hasErrors()) {
                        return String.format("redirect:profiles/%s/skills/create", profileId);
                    }

                    UserAccount owner = optionalOwner.get();
                    skill.setOwner(owner);
                    skillService.createSkill(skill);
                    return "redirect:/profiles/" + owner.getId();
                }
            }
        }
        return "error";
    }

    @GetMapping("profiles/{profileId}/skills/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, Principal principal, @PathVariable String profileId) {
        if (principal != null) {
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());
            Skill skill = skillService.getSkill(id);
            UserAccount owner = skill.getOwner();
            if (Objects.equals(owner.getUsername(), principal.getName()) || user.get().isAdmin()) {
                model.addAttribute("skill", skill);
                model.addAttribute("userId", profileId);
                model.addAttribute("isAdmin", user.get().isAdmin());
                model.addAttribute("edit", true);
                return "skill/form";
            }
        }
        return "error";
    }

    @PostMapping("profiles/{profileId}/skills/edit/{id}")
    public String updateSkill(@PathVariable Integer profileId, @PathVariable Integer id,@Valid @ModelAttribute Skill skill, BindingResult bindingResult, Principal principal) {
        if (principal != null) {
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());
            Skill existingSkill = skillService.getSkill(skill.getId());
            UserAccount owner = existingSkill.getOwner();
            if (Objects.equals(owner.getUsername(), principal.getName()) || user.get().isAdmin()) {
                if (bindingResult.hasErrors()) {
                    return String.format("redirect:profiles/%s/skills/edit/", profileId) + id;
                }
                skill.setOwner(owner);
                skillService.updateSkill(skill);
                return "redirect:/profiles/" + owner.getId();
            }
        }
        return "error";
    }

    @GetMapping("profiles/{profileId}/skills/delete/{id}")
    public String deleteSkill(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal != null) {
            Optional<UserAccount> user = userAccountDetailsService.getUserByUsername(principal.getName());
            Skill skill = skillService.getSkill(id);
            UserAccount owner = skill.getOwner();
            if (Objects.equals(owner.getUsername(), principal.getName()) || user.get().isAdmin()) {
                skillService.deleteSkill(id);
                redirectAttributes.addFlashAttribute("successMessage", "Dovednost úspěšně odstraněna");
                return "redirect:/profiles/" + owner.getId();
            }
        }
        return "error";
    }
}

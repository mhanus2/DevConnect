package com.example.devconnect.service;

import com.example.devconnect.model.Project;
import com.example.devconnect.model.Skill;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAllSkills(UserAccount owner) {
        return skillRepository.findByOwner(owner);
    }

    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill getSkill(Integer id) {
        return skillRepository.findById(id).orElse(null);
    }

    public Skill updateSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public void deleteSkill(Integer id) {
        skillRepository.deleteById(id);
    }
}

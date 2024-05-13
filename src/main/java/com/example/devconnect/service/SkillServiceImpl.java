package com.example.devconnect.service;

import com.example.devconnect.model.Skill;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAllSkills(UserAccount owner) {
        return skillRepository.findByOwner(owner);
    }

    public void createSkill(Skill skill) {
        skillRepository.save(skill);
    }

    public Skill getSkill(Integer id) {
        return skillRepository.findById(id).orElse(null);
    }

    public void updateSkill(Skill skill) {
        skillRepository.save(skill);
    }

    public void deleteSkill(Integer id) {
        skillRepository.deleteById(id);
    }
}

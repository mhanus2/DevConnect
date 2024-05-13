package com.example.devconnect.service;

import com.example.devconnect.model.Skill;
import com.example.devconnect.model.UserAccount;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SkillService {
    List<Skill> getAllSkills(UserAccount owner);

    void createSkill(Skill skill);

    Skill getSkill(Integer id);

    void updateSkill(Skill skill);

    void deleteSkill(Integer id);
}

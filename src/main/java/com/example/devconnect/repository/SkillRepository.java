package com.example.devconnect.repository;

import com.example.devconnect.model.Project;
import com.example.devconnect.model.Skill;
import com.example.devconnect.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {

    List<Skill> findByOwner(UserAccount owner);
}

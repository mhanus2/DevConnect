package com.example.devconnect.service;

import com.example.devconnect.model.Project;
import com.example.devconnect.model.Skill;
import com.example.devconnect.model.Tag;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag createSkill(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag getTag(Integer id) {
        return tagRepository.findById(id).orElse(null);
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
}

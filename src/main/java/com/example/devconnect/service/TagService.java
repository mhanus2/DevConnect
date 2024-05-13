package com.example.devconnect.service;

import com.example.devconnect.model.*;
import com.example.devconnect.repository.PopularTagRepository;
import com.example.devconnect.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final PopularTagRepository popularTagRepository;

    public TagService(TagRepository tagRepository, PopularTagRepository popularTagRepository) {
        this.tagRepository = tagRepository;
        this.popularTagRepository = popularTagRepository;
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

    public Tag updateTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public void delete(Integer id) {
        tagRepository.deleteById(id);
    }

    public List<PopularTag> getPopularTags() {
        return popularTagRepository.findAll();
    }
}

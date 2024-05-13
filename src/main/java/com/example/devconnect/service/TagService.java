package com.example.devconnect.service;

import com.example.devconnect.model.PopularTag;
import com.example.devconnect.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    void createSkill(Tag tag);

    Tag getTag(Integer id);

    List<Tag> getAllTags();

    void updateTag(Tag tag);

    void delete(Integer id);

    List<PopularTag> getPopularTags();
}

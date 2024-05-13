package com.example.devconnect.service;

import com.example.devconnect.model.Image;
import com.example.devconnect.model.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {
    void save(Image image);

    List<Image> getAllImages(Project project);

    void delete(Integer imageId);
}

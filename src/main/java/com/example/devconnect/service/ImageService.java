package com.example.devconnect.service;

import com.example.devconnect.model.Comment;
import com.example.devconnect.model.Image;
import com.example.devconnect.model.Project;
import com.example.devconnect.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void save(Image image) {
        imageRepository.save(image);
    }

    public List<Image> getAllImages(Project project) {
        return imageRepository.findByProject(project);
    }

    public void delete(Integer imageId) {
        String uploadDir = "C:/Users/marti/IdeaProjects/media/";

        Image image = imageRepository.findById(imageId).orElse(null);
        if (image != null) {
            try {
                Files.deleteIfExists(Paths.get(uploadDir + image.getFilePath()));
            } catch (IOException e) {
                e.printStackTrace();
                // todo - neco
                // Handle the exception, e.g. log the error or throw a custom exception
            }
        }

        imageRepository.deleteById(imageId);
    }
}

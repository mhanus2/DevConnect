package com.example.devconnect.repository;

import com.example.devconnect.model.Image;
import com.example.devconnect.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByProject(Project project);
}

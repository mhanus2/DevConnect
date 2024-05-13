package com.example.devconnect.service;

import com.example.devconnect.model.Project;
import com.example.devconnect.model.UserAccount;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    List<Project> getAllProjects();

    Project getProjectById(Integer id);

    List<Project> getProjectsByOwner(UserAccount owner);

    void saveProject(Project project);

    void editProject(Project project);

    void deleteProject(Integer id);
}
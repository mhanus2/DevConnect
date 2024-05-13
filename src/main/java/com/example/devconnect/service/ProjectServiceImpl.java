package com.example.devconnect.service;

import com.example.devconnect.model.Project;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Integer id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.orElse(null);
    }

    public List<Project> getProjectsByOwner(UserAccount owner) {
        return projectRepository.findByOwner(owner);
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public void editProject(Project project) {
        projectRepository.save(project);
    }

    public void deleteProject(Integer id) {
        projectRepository.deleteById(id);
    }
}
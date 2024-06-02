package com.example.devconnect;

import com.example.devconnect.model.Project;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.repository.ProjectRepository;
import com.example.devconnect.service.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DevConnectApplicationTests {
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProjects() {
        Project project1 = new Project();
        Project project2 = new Project();
        List<Project> projects = Arrays.asList(project1, project2);

        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.getAllProjects();
        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void getProjectById() {
        Project project = new Project();
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1);
        assertNotNull(result);
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void getProjectById_NotFound() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        Project result = projectService.getProjectById(1);
        assertNull(result);
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void getProjectsByOwner() {
        UserAccount owner = new UserAccount();
        Project project1 = new Project();
        Project project2 = new Project();
        List<Project> projects = Arrays.asList(project1, project2);

        when(projectRepository.findByOwner(owner)).thenReturn(projects);

        List<Project> result = projectService.getProjectsByOwner(owner);
        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findByOwner(owner);
    }

    @Test
    void saveProject() {
        Project project = new Project();
        projectService.saveProject(project);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void editProject() {
        Project project = new Project();
        projectService.editProject(project);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void deleteProject() {
        projectService.deleteProject(1);
        verify(projectRepository, times(1)).deleteById(1);
    }
}

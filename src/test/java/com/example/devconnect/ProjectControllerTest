package com.example.devconnect;

import com.example.devconnect.controller.ProjectController;
import com.example.devconnect.model.Project;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock private ProjectService projectService;
    @Mock private UserAccountDetailsService userAccountDetailsService;
    @Mock private TagService tagService;
    @Mock private CommentService commentService;
    @Mock private ImageService imageService;
    @Mock private Principal principal;
    @Mock private BindingResult bindingResult;

    @InjectMocks
    private ProjectController projectController;

    @Test
    void editProject_AsOwner_Success() {
        // Příprava uživatele (vlastník)
        UserAccount owner = new UserAccount();
        owner.setUsername("owner");
        owner.setRole("USER");

        Project project = new Project();
        project.setId(1);
        project.setOwner(owner);

        when(projectService.getProjectById(1)).thenReturn(project);
        when(principal.getName()).thenReturn("owner");
        when(userAccountDetailsService.getUserByUsername("owner")).thenReturn(Optional.of(owner));
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = projectController.editProject(project, bindingResult, principal);

        assertEquals("redirect:/projects/1", viewName);
        verify(projectService).editProject(project);
    }

    @Test
    void editProject_AsAdminButNotOwner_Success() {
        // Příprava uživatele (admin, který není vlastníkem)
        UserAccount owner = new UserAccount();
        owner.setUsername("owner");

        UserAccount admin = new UserAccount();
        admin.setUsername("admin");
        admin.setRole("ADMIN"); // Admin má přístup všude

        Project project = new Project();
        project.setId(1);
        project.setOwner(owner);

        when(projectService.getProjectById(1)).thenReturn(project);
        when(principal.getName()).thenReturn("admin");
        when(userAccountDetailsService.getUserByUsername("admin")).thenReturn(Optional.of(admin));

        String viewName = projectController.editProject(project, bindingResult, principal);

        assertEquals("redirect:/projects/1", viewName);
        verify(projectService).editProject(project);
    }
}

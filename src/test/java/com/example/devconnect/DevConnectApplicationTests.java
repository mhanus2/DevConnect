package com.example.devconnect;

import com.example.devconnect.model.Project;
import com.example.devconnect.model.UserAccount;
import com.example.devconnect.service.ProjectService;
import com.example.devconnect.service.UserAccountDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class DevConnectApplicationTests {
    private final UserAccountDetailsService userAccountDetailsService;
    private final ProjectService projectService;

    DevConnectApplicationTests(UserAccountDetailsService userAccountDetailsService, ProjectService projectService) {
        this.userAccountDetailsService = userAccountDetailsService;
        this.projectService = projectService;
    }

    @Test
    public void testCreateAndDeleteProject() {
        // Arrange
        Project project = new Project();
        project.setId(1);
        project.setTitle("Test Project");

        UserAccount user = new UserAccount();
        user.setUsername("testUser");

        when(userAccountDetailsService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        // Act
        projectService.saveProject(project);

        // Assert
        verify(projectService, times(1)).saveProject(any(Project.class));

        // Act
        projectService.deleteProject(project.getId());

        // Assert
        verify(projectService, times(1)).deleteProject(project.getId());
    }


}

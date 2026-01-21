package com.backend.portfolio.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.entities.Project;
import com.backend.portfolio.models.entities.Technology;
import com.backend.portfolio.models.requests.ProjectRequestInsert;
import com.backend.portfolio.models.responses.ProjectResponse;
import com.backend.portfolio.models.updates.ProjectRequestUpdate;
import com.backend.portfolio.repositories.ProfileRepository;
import com.backend.portfolio.repositories.ProjectRepository;
import com.backend.portfolio.repositories.TechnologyRepository;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private TechnologyRepository technologyRepository;

    @InjectMocks
    private ProjectService projectService;

    private Profile testProfile;
    private Project testProject;
    private Technology testTechnology;
    private ProjectRequestInsert testRequestInsert;
    private ProjectRequestUpdate testRequestUpdate;

    @BeforeEach
    void setUp() {
        testProfile = Profile.builder()
                .id(1L)
                .fname("John")
                .lname("Doe")
                .build();

        testTechnology = Technology.builder()
                .id(1L)
                .name("Spring Boot")
                .category("Backend")
                .build();

        testProject = Project.builder()
                .id(1L)
                .name("E-commerce Platform")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .url("http://example.com")
                .banner("banner.jpg")
                .github("github.com/project")
                .profile(testProfile)
                .technologies(new HashSet<>())
                .projectPoints(new HashSet<>())
                .build();

        testRequestInsert = ProjectRequestInsert.builder()
                .name("E-commerce Platform")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .url("http://example.com")
                .banner("banner.jpg")
                .github("github.com/project")
                .profileId(1L)
                .technologyIds(new HashSet<>(Arrays.asList(1L)))
                .projectPoints(new HashSet<>(Arrays.asList("Implemented payment gateway")))
                .build();

        testRequestUpdate = ProjectRequestUpdate.builder()
                .name("E-commerce Platform v2")
                .build();
    }

    @Test
    void getAllProjects_ShouldReturnListOfProjects() {
        // Arrange
        List<Project> projects = Arrays.asList(testProject);
        when(projectRepository.findAllByOrderByCreatedAtDesc()).thenReturn(projects);

        // Act
        List<ProjectResponse> result = projectService.getAllProjects();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getAllProjectsByProfileId_ShouldReturnListOfProjects() {
        // Arrange
        List<Project> projects = Arrays.asList(testProject);
        when(projectRepository.findAllByProfileIdOrderByStartDateDesc(1L)).thenReturn(projects);

        // Act
        List<ProjectResponse> result = projectService.getAllProjectsByProfileId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository, times(1)).findAllByProfileIdOrderByStartDateDesc(1L);
    }

    @Test
    void getProjectById_WhenExists_ShouldReturnProject() {
        // Arrange
        when(projectRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(testProject));

        // Act
        ProjectResponse result = projectService.getProjectById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testProject.getName(), result.getName());
        verify(projectRepository, times(1)).findByIdWithDetails(1L);
    }

    @Test
    void getProjectById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(projectRepository.findByIdWithDetails(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            projectService.getProjectById(999L));
        verify(projectRepository, times(1)).findByIdWithDetails(999L);
    }

    @Test
    void createProject_WithValidData_ShouldReturnCreatedProject() {
        // Arrange
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(technologyRepository.findById(1L)).thenReturn(Optional.of(testTechnology));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        // Act
        ProjectResponse result = projectService.createProject(testRequestInsert);

        // Assert
        assertNotNull(result);
        assertEquals(testProject.getName(), result.getName());
        verify(profileRepository, times(1)).findById(1L);
        verify(technologyRepository, times(1)).findById(1L);
        verify(projectRepository, times(2)).save(any(Project.class)); // Once for creation, once for adding points
    }

    @Test
    void createProject_WithInvalidProfileId_ShouldThrowException() {
        // Arrange
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            projectService.createProject(testRequestInsert));
        verify(profileRepository, times(1)).findById(1L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void createProject_WithInvalidTechnologyId_ShouldThrowException() {
        // Arrange
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(technologyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            projectService.createProject(testRequestInsert));
        verify(profileRepository, times(1)).findById(1L);
        verify(technologyRepository, times(1)).findById(1L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void updateProject_WithValidData_ShouldReturnUpdatedProject() {
        // Arrange
        when(projectRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        // Act
        ProjectResponse result = projectService.updateProject(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(projectRepository, times(1)).findByIdWithDetails(1L);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void updateProject_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(projectRepository.findByIdWithDetails(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            projectService.updateProject(999L, testRequestUpdate));
        verify(projectRepository, times(1)).findByIdWithDetails(999L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void updateProject_WithTechnologyIds_ShouldUpdateTechnologies() {
        // Arrange
        testRequestUpdate.setTechnologyIds(new HashSet<>(Arrays.asList(1L)));
        testProject.getTechnologies().add(testTechnology);
        
        when(projectRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(testProject));
        when(technologyRepository.findById(1L)).thenReturn(Optional.of(testTechnology));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        // Act
        ProjectResponse result = projectService.updateProject(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(projectRepository, times(1)).findByIdWithDetails(1L);
        verify(technologyRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void deleteProject_WhenExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(projectRepository.existsById(1L)).thenReturn(true);
        doNothing().when(projectRepository).deleteById(1L);

        // Act
        projectService.deleteProject(1L);

        // Assert
        verify(projectRepository, times(1)).existsById(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProject_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(projectRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            projectService.deleteProject(999L));
        verify(projectRepository, times(1)).existsById(999L);
        verify(projectRepository, never()).deleteById(anyLong());
    }
}


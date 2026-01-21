package com.backend.portfolio.services;

import java.util.Arrays;
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
import com.backend.portfolio.models.entities.Project;
import com.backend.portfolio.models.entities.ProjectPoint;
import com.backend.portfolio.models.requests.ProjectPointRequestInsert;
import com.backend.portfolio.models.responses.ProjectPointResponse;
import com.backend.portfolio.models.updates.ProjectPointRequestUpdate;
import com.backend.portfolio.repositories.ProjectPointRepository;
import com.backend.portfolio.repositories.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class ProjectPointServiceTest {

    @Mock
    private ProjectPointRepository projectPointRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectPointService projectPointService;

    private Project testProject;
    private ProjectPoint testProjectPoint;
    private ProjectPointRequestInsert testRequestInsert;
    private ProjectPointRequestUpdate testRequestUpdate;

    @BeforeEach
    void setUp() {
        testProject = Project.builder()
                .id(1L)
                .name("E-commerce Platform")
                .build();

        testProjectPoint = ProjectPoint.builder()
                .id(1L)
                .content("Implemented payment gateway integration")
                .project(testProject)
                .build();

        testRequestInsert = ProjectPointRequestInsert.builder()
                .content("Implemented payment gateway integration")
                .projectId(1L)
                .build();

        testRequestUpdate = ProjectPointRequestUpdate.builder()
                .content("Implemented payment and shipping gateway integration")
                .build();
    }

    @Test
    void getAllProjectPointsByProjectId_ShouldReturnListOfProjectPoints() {
        // Arrange
        List<ProjectPoint> projectPoints = Arrays.asList(testProjectPoint);
        when(projectPointRepository.findAllByProjectIdOrderByCreatedAtAsc(1L)).thenReturn(projectPoints);

        // Act
        List<ProjectPointResponse> result = projectPointService.getAllProjectPointsByProjectId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectPointRepository, times(1)).findAllByProjectIdOrderByCreatedAtAsc(1L);
    }

    @Test
    void getProjectPointById_WhenExists_ShouldReturnProjectPoint() {
        // Arrange
        when(projectPointRepository.findById(1L)).thenReturn(Optional.of(testProjectPoint));

        // Act
        ProjectPointResponse result = projectPointService.getProjectPointById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testProjectPoint.getContent(), result.getContent());
        verify(projectPointRepository, times(1)).findById(1L);
    }

    @Test
    void getProjectPointById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(projectPointRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            projectPointService.getProjectPointById(999L));
        verify(projectPointRepository, times(1)).findById(999L);
    }

    @Test
    void createProjectPoint_WithValidData_ShouldReturnCreatedProjectPoint() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectPointRepository.save(any(ProjectPoint.class))).thenReturn(testProjectPoint);

        // Act
        ProjectPointResponse result = projectPointService.createProjectPoint(testRequestInsert);

        // Assert
        assertNotNull(result);
        assertEquals(testProjectPoint.getContent(), result.getContent());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectPointRepository, times(1)).save(any(ProjectPoint.class));
    }

    @Test
    void createProjectPoint_WithInvalidProjectId_ShouldThrowException() {
        // Arrange
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            projectPointService.createProjectPoint(testRequestInsert));
        verify(projectRepository, times(1)).findById(1L);
        verify(projectPointRepository, never()).save(any(ProjectPoint.class));
    }

    @Test
    void updateProjectPoint_WithValidData_ShouldReturnUpdatedProjectPoint() {
        // Arrange
        when(projectPointRepository.findById(1L)).thenReturn(Optional.of(testProjectPoint));
        when(projectPointRepository.save(any(ProjectPoint.class))).thenReturn(testProjectPoint);

        // Act
        ProjectPointResponse result = projectPointService.updateProjectPoint(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(projectPointRepository, times(1)).findById(1L);
        verify(projectPointRepository, times(1)).save(any(ProjectPoint.class));
    }

    @Test
    void updateProjectPoint_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(projectPointRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            projectPointService.updateProjectPoint(999L, testRequestUpdate));
        verify(projectPointRepository, times(1)).findById(999L);
        verify(projectPointRepository, never()).save(any(ProjectPoint.class));
    }

    @Test
    void updateProjectPoint_WithProjectIdChange_ShouldUpdateProject() {
        // Arrange
        testRequestUpdate.setProjectId(2L);
        Project newProject = Project.builder().id(2L).name("New Project").build();
        
        when(projectPointRepository.findById(1L)).thenReturn(Optional.of(testProjectPoint));
        when(projectRepository.findById(2L)).thenReturn(Optional.of(newProject));
        when(projectPointRepository.save(any(ProjectPoint.class))).thenReturn(testProjectPoint);

        // Act
        ProjectPointResponse result = projectPointService.updateProjectPoint(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(projectPointRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).findById(2L);
        verify(projectPointRepository, times(1)).save(any(ProjectPoint.class));
    }

    @Test
    void deleteProjectPoint_WhenExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(projectPointRepository.existsById(1L)).thenReturn(true);
        doNothing().when(projectPointRepository).deleteById(1L);

        // Act
        projectPointService.deleteProjectPoint(1L);

        // Assert
        verify(projectPointRepository, times(1)).existsById(1L);
        verify(projectPointRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProjectPoint_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(projectPointRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            projectPointService.deleteProjectPoint(999L));
        verify(projectPointRepository, times(1)).existsById(999L);
        verify(projectPointRepository, never()).deleteById(anyLong());
    }
}


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
import com.backend.portfolio.models.entities.Experience;
import com.backend.portfolio.models.entities.ExperiencePoint;
import com.backend.portfolio.models.requests.ExperiencePointRequestInsert;
import com.backend.portfolio.models.responses.ExperiencePointResponse;
import com.backend.portfolio.models.updates.ExperiencePointRequestUpdate;
import com.backend.portfolio.repositories.ExperiencePointRepository;
import com.backend.portfolio.repositories.ExperienceRepository;

@ExtendWith(MockitoExtension.class)
class ExperiencePointServiceTest {

    @Mock
    private ExperiencePointRepository experiencePointRepository;

    @Mock
    private ExperienceRepository experienceRepository;

    @InjectMocks
    private ExperiencePointService experiencePointService;

    private Experience testExperience;
    private ExperiencePoint testExperiencePoint;
    private ExperiencePointRequestInsert testRequestInsert;
    private ExperiencePointRequestUpdate testRequestUpdate;

    @BeforeEach
    void setUp() {
        testExperience = Experience.builder()
                .id(1L)
                .company("Tech Corp")
                .position("Software Engineer")
                .build();

        testExperiencePoint = ExperiencePoint.builder()
                .id(1L)
                .content("Led team of 5 developers")
                .experience(testExperience)
                .build();

        testRequestInsert = ExperiencePointRequestInsert.builder()
                .content("Led team of 5 developers")
                .experienceId(1L)
                .build();

        testRequestUpdate = ExperiencePointRequestUpdate.builder()
                .content("Led team of 10 developers")
                .build();
    }

    @Test
    void getAllExperiencePointsByExperienceId_ShouldReturnListOfExperiencePoints() {
        // Arrange
        List<ExperiencePoint> experiencePoints = Arrays.asList(testExperiencePoint);
        when(experiencePointRepository.findAllByExperienceIdOrderByCreatedAtAsc(1L)).thenReturn(experiencePoints);

        // Act
        List<ExperiencePointResponse> result = experiencePointService.getAllExperiencePointsByExperienceId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(experiencePointRepository, times(1)).findAllByExperienceIdOrderByCreatedAtAsc(1L);
    }

    @Test
    void getExperiencePointById_WhenExists_ShouldReturnExperiencePoint() {
        // Arrange
        when(experiencePointRepository.findById(1L)).thenReturn(Optional.of(testExperiencePoint));

        // Act
        ExperiencePointResponse result = experiencePointService.getExperiencePointById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testExperiencePoint.getContent(), result.getContent());
        verify(experiencePointRepository, times(1)).findById(1L);
    }

    @Test
    void getExperiencePointById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(experiencePointRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            experiencePointService.getExperiencePointById(999L));
        verify(experiencePointRepository, times(1)).findById(999L);
    }

    @Test
    void createExperiencePoint_WithValidData_ShouldReturnCreatedExperiencePoint() {
        // Arrange
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(testExperience));
        when(experiencePointRepository.save(any(ExperiencePoint.class))).thenReturn(testExperiencePoint);

        // Act
        ExperiencePointResponse result = experiencePointService.createExperiencePoint(testRequestInsert);

        // Assert
        assertNotNull(result);
        assertEquals(testExperiencePoint.getContent(), result.getContent());
        verify(experienceRepository, times(1)).findById(1L);
        verify(experiencePointRepository, times(1)).save(any(ExperiencePoint.class));
    }

    @Test
    void createExperiencePoint_WithInvalidExperienceId_ShouldThrowException() {
        // Arrange
        when(experienceRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            experiencePointService.createExperiencePoint(testRequestInsert));
        verify(experienceRepository, times(1)).findById(1L);
        verify(experiencePointRepository, never()).save(any(ExperiencePoint.class));
    }

    @Test
    void updateExperiencePoint_WithValidData_ShouldReturnUpdatedExperiencePoint() {
        // Arrange
        when(experiencePointRepository.findById(1L)).thenReturn(Optional.of(testExperiencePoint));
        when(experiencePointRepository.save(any(ExperiencePoint.class))).thenReturn(testExperiencePoint);

        // Act
        ExperiencePointResponse result = experiencePointService.updateExperiencePoint(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(experiencePointRepository, times(1)).findById(1L);
        verify(experiencePointRepository, times(1)).save(any(ExperiencePoint.class));
    }

    @Test
    void updateExperiencePoint_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(experiencePointRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            experiencePointService.updateExperiencePoint(999L, testRequestUpdate));
        verify(experiencePointRepository, times(1)).findById(999L);
        verify(experiencePointRepository, never()).save(any(ExperiencePoint.class));
    }

    @Test
    void updateExperiencePoint_WithExperienceIdChange_ShouldUpdateExperience() {
        // Arrange
        testRequestUpdate.setExperienceId(2L);
        Experience newExperience = Experience.builder().id(2L).company("New Corp").build();
        
        when(experiencePointRepository.findById(1L)).thenReturn(Optional.of(testExperiencePoint));
        when(experienceRepository.findById(2L)).thenReturn(Optional.of(newExperience));
        when(experiencePointRepository.save(any(ExperiencePoint.class))).thenReturn(testExperiencePoint);

        // Act
        ExperiencePointResponse result = experiencePointService.updateExperiencePoint(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(experiencePointRepository, times(1)).findById(1L);
        verify(experienceRepository, times(1)).findById(2L);
        verify(experiencePointRepository, times(1)).save(any(ExperiencePoint.class));
    }

    @Test
    void deleteExperiencePoint_WhenExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(experiencePointRepository.existsById(1L)).thenReturn(true);
        doNothing().when(experiencePointRepository).deleteById(1L);

        // Act
        experiencePointService.deleteExperiencePoint(1L);

        // Assert
        verify(experiencePointRepository, times(1)).existsById(1L);
        verify(experiencePointRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteExperiencePoint_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(experiencePointRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            experiencePointService.deleteExperiencePoint(999L));
        verify(experiencePointRepository, times(1)).existsById(999L);
        verify(experiencePointRepository, never()).deleteById(anyLong());
    }
}


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
import com.backend.portfolio.models.entities.Experience;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.entities.Technology;
import com.backend.portfolio.models.requests.ExperienceRequestInsert;
import com.backend.portfolio.models.responses.ExperienceResponse;
import com.backend.portfolio.models.updates.ExperienceRequestUpdate;
import com.backend.portfolio.repositories.ExperienceRepository;
import com.backend.portfolio.repositories.ProfileRepository;
import com.backend.portfolio.repositories.TechnologyRepository;

@ExtendWith(MockitoExtension.class)
class ExperienceServiceTest {

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private TechnologyRepository technologyRepository;

    @InjectMocks
    private ExperienceService experienceService;

    private Profile testProfile;
    private Experience testExperience;
    private Technology testTechnology;
    private ExperienceRequestInsert testRequestInsert;
    private ExperienceRequestUpdate testRequestUpdate;

    @BeforeEach
    void setUp() {
        testProfile = Profile.builder()
                .id(1L)
                .fname("John")
                .lname("Doe")
                .build();

        testTechnology = Technology.builder()
                .id(1L)
                .name("Java")
                .category("Backend")
                .build();

        testExperience = Experience.builder()
                .id(1L)
                .company("Tech Corp")
                .position("Software Engineer")
                .startDate(LocalDate.of(2020, 1, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .location("New York, NY")
                .url("http://techcorp.com")
                .banner("banner.jpg")
                .github("github.com/techcorp")
                .profile(testProfile)
                .technologies(new HashSet<>())
                .experiencePoints(new HashSet<>())
                .build();

        testRequestInsert = ExperienceRequestInsert.builder()
                .company("Tech Corp")
                .position("Software Engineer")
                .startDate(LocalDate.of(2020, 1, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .location("New York, NY")
                .url("http://techcorp.com")
                .banner("banner.jpg")
                .github("github.com/techcorp")
                .profileId(1L)
                .technologyIds(new HashSet<>(Arrays.asList(1L)))
                .experiencePoints(new HashSet<>(Arrays.asList("Led team of 5 developers")))
                .build();

        testRequestUpdate = ExperienceRequestUpdate.builder()
                .company("Tech Corp Updated")
                .position("Senior Software Engineer")
                .build();
    }

    @Test
    void getAllExperiences_ShouldReturnListOfExperiences() {
        // Arrange
        List<Experience> experiences = Arrays.asList(testExperience);
        when(experienceRepository.findAllByOrderByCreatedAtDesc()).thenReturn(experiences);

        // Act
        List<ExperienceResponse> result = experienceService.getAllExperiences();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(experienceRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getAllExperiencesByProfileId_ShouldReturnListOfExperiences() {
        // Arrange
        List<Experience> experiences = Arrays.asList(testExperience);
        when(experienceRepository.findAllByProfileIdOrderByStartDateDesc(1L)).thenReturn(experiences);

        // Act
        List<ExperienceResponse> result = experienceService.getAllExperiencesByProfileId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(experienceRepository, times(1)).findAllByProfileIdOrderByStartDateDesc(1L);
    }

    @Test
    void getExperienceById_WhenExists_ShouldReturnExperience() {
        // Arrange
        when(experienceRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(testExperience));

        // Act
        ExperienceResponse result = experienceService.getExperienceById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testExperience.getCompany(), result.getCompany());
        verify(experienceRepository, times(1)).findByIdWithDetails(1L);
    }

    @Test
    void getExperienceById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(experienceRepository.findByIdWithDetails(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            experienceService.getExperienceById(999L));
        verify(experienceRepository, times(1)).findByIdWithDetails(999L);
    }

    @Test
    void createExperience_WithValidData_ShouldReturnCreatedExperience() {
        // Arrange
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(technologyRepository.findById(1L)).thenReturn(Optional.of(testTechnology));
        when(experienceRepository.save(any(Experience.class))).thenReturn(testExperience);

        // Act
        ExperienceResponse result = experienceService.createExperience(testRequestInsert);

        // Assert
        assertNotNull(result);
        assertEquals(testExperience.getCompany(), result.getCompany());
        verify(profileRepository, times(1)).findById(1L);
        verify(technologyRepository, times(1)).findById(1L);
        verify(experienceRepository, times(2)).save(any(Experience.class)); // Once for creation, once for adding points
    }

    @Test
    void createExperience_WithInvalidProfileId_ShouldThrowException() {
        // Arrange
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            experienceService.createExperience(testRequestInsert));
        verify(profileRepository, times(1)).findById(1L);
        verify(experienceRepository, never()).save(any(Experience.class));
    }

    @Test
    void createExperience_WithInvalidTechnologyId_ShouldThrowException() {
        // Arrange
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(technologyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            experienceService.createExperience(testRequestInsert));
        verify(profileRepository, times(1)).findById(1L);
        verify(technologyRepository, times(1)).findById(1L);
        verify(experienceRepository, never()).save(any(Experience.class));
    }

    @Test
    void updateExperience_WithValidData_ShouldReturnUpdatedExperience() {
        // Arrange
        when(experienceRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(testExperience));
        when(experienceRepository.save(any(Experience.class))).thenReturn(testExperience);

        // Act
        ExperienceResponse result = experienceService.updateExperience(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(experienceRepository, times(1)).findByIdWithDetails(1L);
        verify(experienceRepository, times(1)).save(any(Experience.class));
    }

    @Test
    void updateExperience_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(experienceRepository.findByIdWithDetails(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            experienceService.updateExperience(999L, testRequestUpdate));
        verify(experienceRepository, times(1)).findByIdWithDetails(999L);
        verify(experienceRepository, never()).save(any(Experience.class));
    }

    @Test
    void updateExperience_WithTechnologyIds_ShouldUpdateTechnologies() {
        // Arrange
        testRequestUpdate.setTechnologyIds(new HashSet<>(Arrays.asList(1L)));
        testExperience.getTechnologies().add(testTechnology);
        
        when(experienceRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(testExperience));
        when(technologyRepository.findById(1L)).thenReturn(Optional.of(testTechnology));
        when(experienceRepository.save(any(Experience.class))).thenReturn(testExperience);

        // Act
        ExperienceResponse result = experienceService.updateExperience(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(experienceRepository, times(1)).findByIdWithDetails(1L);
        verify(technologyRepository, times(1)).findById(1L);
        verify(experienceRepository, times(1)).save(any(Experience.class));
    }

    @Test
    void deleteExperience_WhenExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(experienceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(experienceRepository).deleteById(1L);

        // Act
        experienceService.deleteExperience(1L);

        // Assert
        verify(experienceRepository, times(1)).existsById(1L);
        verify(experienceRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteExperience_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(experienceRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            experienceService.deleteExperience(999L));
        verify(experienceRepository, times(1)).existsById(999L);
        verify(experienceRepository, never()).deleteById(anyLong());
    }
}


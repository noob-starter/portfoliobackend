package com.backend.portfolio.services;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.backend.portfolio.models.entities.Education;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.EducationRequestInsert;
import com.backend.portfolio.models.responses.EducationResponse;
import com.backend.portfolio.models.updates.EducationRequestUpdate;
import com.backend.portfolio.repositories.EducationRepository;
import com.backend.portfolio.repositories.ProfileRepository;

@ExtendWith(MockitoExtension.class)
class EducationServiceTest {

    @Mock
    private EducationRepository educationRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private EducationService educationService;

    private Profile testProfile;
    private Education testEducation;
    private EducationRequestInsert testRequestInsert;
    private EducationRequestUpdate testRequestUpdate;

    @BeforeEach
    void setUp() {
        testProfile = Profile.builder()
                .id(1L)
                .fname("John")
                .lname("Doe")
                .build();

        testEducation = Education.builder()
                .id(1L)
                .degree("Bachelor of Science")
                .institution("Test University")
                .field("Computer Science")
                .startDate(LocalDate.of(2018, 9, 1))
                .endDate(LocalDate.of(2022, 6, 1))
                .percentage(BigDecimal.valueOf(3.8))
                .description("Test Description")
                .url("http://test-uni.edu")
                .banner("banner.jpg")
                .github("github.com/test")
                .profile(testProfile)
                .build();

        testRequestInsert = EducationRequestInsert.builder()
                .degree("Bachelor of Science")
                .institution("Test University")
                .field("Computer Science")
                .startDate(LocalDate.of(2018, 9, 1))
                .endDate(LocalDate.of(2022, 6, 1))
                .percentage(BigDecimal.valueOf(3.8))
                .description("Test Description")
                .url("http://test-uni.edu")
                .banner("banner.jpg")
                .github("github.com/test")
                .profileId(1L)
                .build();

        testRequestUpdate = EducationRequestUpdate.builder()
                .degree("Master of Science")
                .percentage(BigDecimal.valueOf(3.9))
                .build();
    }

    @Test
    void getAllEducations_ShouldReturnListOfEducations() {
        // Arrange
        List<Education> educations = Arrays.asList(testEducation);
        when(educationRepository.findAll()).thenReturn(educations);

        // Act
        List<EducationResponse> result = educationService.getAllEducations();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(educationRepository, times(1)).findAll();
    }

    @Test
    void getEducationById_WhenExists_ShouldReturnEducation() {
        // Arrange
        when(educationRepository.findById(1L)).thenReturn(Optional.of(testEducation));

        // Act
        EducationResponse result = educationService.getEducationById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testEducation.getDegree(), result.getDegree());
        verify(educationRepository, times(1)).findById(1L);
    }

    @Test
    void getEducationById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(educationRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            educationService.getEducationById(999L));
        verify(educationRepository, times(1)).findById(999L);
    }

    @Test
    void getAllEducationsByProfileId_ShouldReturnListOfEducations() {
        // Arrange
        List<Education> educations = Arrays.asList(testEducation);
        when(educationRepository.findAllByProfileIdOrderByStartDateDesc(1L)).thenReturn(educations);

        // Act
        List<EducationResponse> result = educationService.getAllEducationsByProfileId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(educationRepository, times(1)).findAllByProfileIdOrderByStartDateDesc(1L);
    }

    @Test
    void createEducation_WithValidData_ShouldReturnCreatedEducation() {
        // Arrange
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(educationRepository.save(any(Education.class))).thenReturn(testEducation);

        // Act
        EducationResponse result = educationService.createEducation(testRequestInsert);

        // Assert
        assertNotNull(result);
        assertEquals(testEducation.getDegree(), result.getDegree());
        verify(profileRepository, times(1)).findById(1L);
        verify(educationRepository, times(1)).save(any(Education.class));
    }

    @Test
    void createEducation_WithInvalidProfileId_ShouldThrowException() {
        // Arrange
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            educationService.createEducation(testRequestInsert));
        verify(profileRepository, times(1)).findById(1L);
        verify(educationRepository, never()).save(any(Education.class));
    }

    @Test
    void updateEducation_WithValidData_ShouldReturnUpdatedEducation() {
        // Arrange
        when(educationRepository.findById(1L)).thenReturn(Optional.of(testEducation));
        when(educationRepository.save(any(Education.class))).thenReturn(testEducation);

        // Act
        EducationResponse result = educationService.updateEducation(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(educationRepository, times(1)).findById(1L);
        verify(educationRepository, times(1)).save(any(Education.class));
    }

    @Test
    void updateEducation_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(educationRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            educationService.updateEducation(999L, testRequestUpdate));
        verify(educationRepository, times(1)).findById(999L);
        verify(educationRepository, never()).save(any(Education.class));
    }

    @Test
    void deleteEducation_WhenExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(educationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(educationRepository).deleteById(1L);

        // Act
        educationService.deleteEducation(1L);

        // Assert
        verify(educationRepository, times(1)).existsById(1L);
        verify(educationRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEducation_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(educationRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            educationService.deleteEducation(999L));
        verify(educationRepository, times(1)).existsById(999L);
        verify(educationRepository, never()).deleteById(anyLong());
    }
}


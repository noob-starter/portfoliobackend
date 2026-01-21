package com.backend.portfolio.services;

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
import com.backend.portfolio.models.entities.Achievement;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.AchievementRequestInsert;
import com.backend.portfolio.models.responses.AchievementResponse;
import com.backend.portfolio.models.updates.AchievementRequestUpdate;
import com.backend.portfolio.repositories.AchievementRepository;
import com.backend.portfolio.repositories.ProfileRepository;

@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private AchievementService achievementService;

    private Profile testProfile;
    private Achievement testAchievement;
    private AchievementRequestInsert testRequestInsert;
    private AchievementRequestUpdate testRequestUpdate;

    @BeforeEach
    void setUp() {
        testProfile = Profile.builder()
                .id(1L)
                .fname("John")
                .lname("Doe")
                .build();

        testAchievement = Achievement.builder()
                .id(1L)
                .name("Test Achievement")
                .dateAchieved(LocalDate.now())
                .issuer("Test Issuer")
                .description("Test Description")
                .url("http://test.com")
                .banner("banner.jpg")
                .github("github.com/test")
                .profile(testProfile)
                .build();

        testRequestInsert = AchievementRequestInsert.builder()
                .name("Test Achievement")
                .dateAchieved(LocalDate.now())
                .issuer("Test Issuer")
                .description("Test Description")
                .url("http://test.com")
                .banner("banner.jpg")
                .github("github.com/test")
                .profileId(1L)
                .build();

        testRequestUpdate = AchievementRequestUpdate.builder()
                .name("Updated Achievement")
                .issuer("Updated Issuer")
                .build();
    }

    @Test
    void getAllAchievements_ShouldReturnListOfAchievements() {
        // Arrange
        List<Achievement> achievements = Arrays.asList(testAchievement);
        when(achievementRepository.findAllByOrderByCreatedAtDesc()).thenReturn(achievements);

        // Act
        List<AchievementResponse> result = achievementService.getAllAchievements();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(achievementRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getAllAchievementsByProfileId_ShouldReturnListOfAchievements() {
        // Arrange
        List<Achievement> achievements = Arrays.asList(testAchievement);
        when(achievementRepository.findAllByProfileIdOrderByDateAchievedDesc(1L)).thenReturn(achievements);

        // Act
        List<AchievementResponse> result = achievementService.getAllAchievementsByProfileId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(achievementRepository, times(1)).findAllByProfileIdOrderByDateAchievedDesc(1L);
    }

    @Test
    void getAchievementById_WhenExists_ShouldReturnAchievement() {
        // Arrange
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(testAchievement));

        // Act
        AchievementResponse result = achievementService.getAchievementById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testAchievement.getName(), result.getName());
        verify(achievementRepository, times(1)).findById(1L);
    }

    @Test
    void getAchievementById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(achievementRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            achievementService.getAchievementById(999L));
        verify(achievementRepository, times(1)).findById(999L);
    }

    @Test
    void createAchievement_WithValidData_ShouldReturnCreatedAchievement() {
        // Arrange
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(achievementRepository.save(any(Achievement.class))).thenReturn(testAchievement);

        // Act
        AchievementResponse result = achievementService.createAchievement(testRequestInsert);

        // Assert
        assertNotNull(result);
        assertEquals(testAchievement.getName(), result.getName());
        verify(profileRepository, times(1)).findById(1L);
        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void createAchievement_WithInvalidProfileId_ShouldThrowException() {
        // Arrange
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            achievementService.createAchievement(testRequestInsert));
        verify(profileRepository, times(1)).findById(1L);
        verify(achievementRepository, never()).save(any(Achievement.class));
    }

    @Test
    void updateAchievement_WithValidData_ShouldReturnUpdatedAchievement() {
        // Arrange
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(testAchievement));
        when(achievementRepository.save(any(Achievement.class))).thenReturn(testAchievement);

        // Act
        AchievementResponse result = achievementService.updateAchievement(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(achievementRepository, times(1)).findById(1L);
        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void updateAchievement_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(achievementRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            achievementService.updateAchievement(999L, testRequestUpdate));
        verify(achievementRepository, times(1)).findById(999L);
        verify(achievementRepository, never()).save(any(Achievement.class));
    }

    @Test
    void updateAchievement_WithProfileIdChange_ShouldUpdateProfile() {
        // Arrange
        testRequestUpdate.setProfileId(2L);
        Profile newProfile = Profile.builder().id(2L).fname("Jane").lname("Doe").build();
        
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(testAchievement));
        when(profileRepository.findById(2L)).thenReturn(Optional.of(newProfile));
        when(achievementRepository.save(any(Achievement.class))).thenReturn(testAchievement);

        // Act
        AchievementResponse result = achievementService.updateAchievement(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(achievementRepository, times(1)).findById(1L);
        verify(profileRepository, times(1)).findById(2L);
        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void deleteAchievement_WhenExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(achievementRepository.existsById(1L)).thenReturn(true);
        doNothing().when(achievementRepository).deleteById(1L);

        // Act
        achievementService.deleteAchievement(1L);

        // Assert
        verify(achievementRepository, times(1)).existsById(1L);
        verify(achievementRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAchievement_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(achievementRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            achievementService.deleteAchievement(999L));
        verify(achievementRepository, times(1)).existsById(999L);
        verify(achievementRepository, never()).deleteById(anyLong());
    }
}


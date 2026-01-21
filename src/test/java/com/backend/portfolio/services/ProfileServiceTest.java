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
import static org.mockito.ArgumentMatchers.anyString;
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
import com.backend.portfolio.models.requests.ProfileRequestInsert;
import com.backend.portfolio.models.responses.ProfileResponse;
import com.backend.portfolio.models.states.enums.Sex;
import com.backend.portfolio.models.updates.ProfileRequestResponse;
import com.backend.portfolio.repositories.ProfileRepository;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    private Profile testProfile;
    private ProfileRequestInsert testRequestInsert;
    private ProfileRequestResponse testRequestUpdate;

    @BeforeEach
    void setUp() {
        testProfile = Profile.builder()
                .id(1L)
                .fname("John")
                .lname("Doe")
                .sex(Sex.MALE)
                .bio("Test Bio")
                .banner("banner.jpg")
                .intro("Test Intro")
                .contour("Test Contour")
                .url("http://test.com")
                .build();

        testRequestInsert = ProfileRequestInsert.builder()
                .fname("John")
                .lname("Doe")
                .sex(Sex.MALE)
                .bio("Test Bio")
                .banner("banner.jpg")
                .intro("Test Intro")
                .contour("Test Contour")
                .url("http://test.com")
                .build();

        testRequestUpdate = ProfileRequestResponse.builder()
                .fname("Jane")
                .lname("Smith")
                .build();
    }

    @Test
    void getAllProfiles_ShouldReturnListOfProfiles() {
        // Arrange
        List<Profile> profiles = Arrays.asList(testProfile);
        when(profileRepository.findAllByOrderByCreatedAtDesc()).thenReturn(profiles);

        // Act
        List<ProfileResponse> result = profileService.getAllProfiles();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(profileRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getProfileById_WhenExists_ShouldReturnProfile() {
        // Arrange
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));

        // Act
        ProfileResponse result = profileService.getProfileById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testProfile.getFname(), result.getFname());
        assertEquals(testProfile.getLname(), result.getLname());
        verify(profileRepository, times(1)).findById(1L);
    }

    @Test
    void getProfileById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            profileService.getProfileById(999L));
        verify(profileRepository, times(1)).findById(999L);
    }

    @Test
    void getProfileByName_WhenExists_ShouldReturnProfile() {
        // Arrange
        when(profileRepository.findByFnameAndLname("John", "Doe"))
                .thenReturn(Optional.of(testProfile));

        // Act
        ProfileResponse result = profileService.getProfileByName("John", "Doe");

        // Assert
        assertNotNull(result);
        assertEquals(testProfile.getFname(), result.getFname());
        assertEquals(testProfile.getLname(), result.getLname());
        verify(profileRepository, times(1)).findByFnameAndLname("John", "Doe");
    }

    @Test
    void getProfileByName_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(profileRepository.findByFnameAndLname(anyString(), anyString()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            profileService.getProfileByName("Unknown", "User"));
        verify(profileRepository, times(1)).findByFnameAndLname("Unknown", "User");
    }

    @Test
    void createProfile_WithValidData_ShouldReturnCreatedProfile() {
        // Arrange
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // Act
        ProfileResponse result = profileService.createProfile(testRequestInsert);

        // Assert
        assertNotNull(result);
        assertEquals(testProfile.getFname(), result.getFname());
        assertEquals(testProfile.getLname(), result.getLname());
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void updateProfile_WithValidData_ShouldReturnUpdatedProfile() {
        // Arrange
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // Act
        ProfileResponse result = profileService.updateProfile(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(profileRepository, times(1)).findById(1L);
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void updateProfile_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            profileService.updateProfile(999L, testRequestUpdate));
        verify(profileRepository, times(1)).findById(999L);
        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    void deleteProfile_WhenExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(profileRepository.existsById(1L)).thenReturn(true);
        doNothing().when(profileRepository).deleteById(1L);

        // Act
        profileService.deleteProfile(1L);

        // Assert
        verify(profileRepository, times(1)).existsById(1L);
        verify(profileRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProfile_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(profileRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            profileService.deleteProfile(999L));
        verify(profileRepository, times(1)).existsById(999L);
        verify(profileRepository, never()).deleteById(anyLong());
    }
}


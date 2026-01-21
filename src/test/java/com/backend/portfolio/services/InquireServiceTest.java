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
import com.backend.portfolio.models.entities.Inquire;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.InquireRequestInsert;
import com.backend.portfolio.models.responses.InquireResponse;
import com.backend.portfolio.repositories.InquireRepository;
import com.backend.portfolio.repositories.ProfileRepository;

@ExtendWith(MockitoExtension.class)
class InquireServiceTest {

    @Mock
    private InquireRepository inquireRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private InquireService inquireService;

    private Profile testProfile;
    private Inquire testInquire;
    private InquireRequestInsert testRequestInsert;

    @BeforeEach
    void setUp() {
        testProfile = Profile.builder()
                .id(1L)
                .fname("John")
                .lname("Doe")
                .build();

        testInquire = Inquire.builder()
                .id(1L)
                .name("Jane Smith")
                .email("jane@example.com")
                .message("I would like to discuss a project opportunity.")
                .profile(testProfile)
                .build();

        testRequestInsert = InquireRequestInsert.builder()
                .name("Jane Smith")
                .email("jane@example.com")
                .message("I would like to discuss a project opportunity.")
                .profileId(1L)
                .build();
    }

    @Test
    void getAllInquires_ShouldReturnListOfInquires() {
        // Arrange
        List<Inquire> inquires = Arrays.asList(testInquire);
        when(inquireRepository.findAllByOrderByCreatedAtDesc()).thenReturn(inquires);

        // Act
        List<InquireResponse> result = inquireService.getAllInquires();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(inquireRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getAllInquiresByProfileId_ShouldReturnListOfInquires() {
        // Arrange
        List<Inquire> inquires = Arrays.asList(testInquire);
        when(inquireRepository.findAllByProfileIdOrderByCreatedAtDesc(1L)).thenReturn(inquires);

        // Act
        List<InquireResponse> result = inquireService.getAllInquiresByProfileId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(inquireRepository, times(1)).findAllByProfileIdOrderByCreatedAtDesc(1L);
    }

    @Test
    void getInquireById_WhenExists_ShouldReturnInquire() {
        // Arrange
        when(inquireRepository.findById(1L)).thenReturn(Optional.of(testInquire));

        // Act
        InquireResponse result = inquireService.getInquireById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testInquire.getName(), result.getName());
        assertEquals(testInquire.getEmail(), result.getEmail());
        verify(inquireRepository, times(1)).findById(1L);
    }

    @Test
    void getInquireById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(inquireRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            inquireService.getInquireById(999L));
        verify(inquireRepository, times(1)).findById(999L);
    }

    @Test
    void createInquire_WithValidData_ShouldReturnCreatedInquire() {
        // Arrange
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(inquireRepository.save(any(Inquire.class))).thenReturn(testInquire);

        // Act
        InquireResponse result = inquireService.createInquire(testRequestInsert);

        // Assert
        assertNotNull(result);
        assertEquals(testInquire.getName(), result.getName());
        assertEquals(testInquire.getEmail(), result.getEmail());
        verify(profileRepository, times(1)).findById(1L);
        verify(inquireRepository, times(1)).save(any(Inquire.class));
    }

    @Test
    void createInquire_WithInvalidProfileId_ShouldThrowException() {
        // Arrange
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            inquireService.createInquire(testRequestInsert));
        verify(profileRepository, times(1)).findById(1L);
        verify(inquireRepository, never()).save(any(Inquire.class));
    }

    @Test
    void deleteInquire_WhenExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(inquireRepository.existsById(1L)).thenReturn(true);
        doNothing().when(inquireRepository).deleteById(1L);

        // Act
        inquireService.deleteInquire(1L);

        // Assert
        verify(inquireRepository, times(1)).existsById(1L);
        verify(inquireRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteInquire_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(inquireRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            inquireService.deleteInquire(999L));
        verify(inquireRepository, times(1)).existsById(999L);
        verify(inquireRepository, never()).deleteById(anyLong());
    }
}


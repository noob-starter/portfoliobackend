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
import com.backend.portfolio.models.entities.Faq;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.FaqRequestInsert;
import com.backend.portfolio.models.responses.FaqResponse;
import com.backend.portfolio.models.updates.FaqRequestUpdate;
import com.backend.portfolio.repositories.FaqRepository;
import com.backend.portfolio.repositories.ProfileRepository;

@ExtendWith(MockitoExtension.class)
class FaqServiceTest {

    @Mock
    private FaqRepository faqRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private FaqService faqService;

    private Profile testProfile;
    private Faq testFaq;
    private FaqRequestInsert testRequestInsert;
    private FaqRequestUpdate testRequestUpdate;

    @BeforeEach
    void setUp() {
        testProfile = Profile.builder()
                .id(1L)
                .fname("John")
                .lname("Doe")
                .build();

        testFaq = Faq.builder()
                .id(1L)
                .question("What is your experience?")
                .answer("I have 5 years of experience in software development.")
                .profile(testProfile)
                .build();

        testRequestInsert = FaqRequestInsert.builder()
                .question("What is your experience?")
                .answer("I have 5 years of experience in software development.")
                .profileId(1L)
                .build();

        testRequestUpdate = FaqRequestUpdate.builder()
                .question("What is your expertise?")
                .answer("I have 6 years of expertise in software development.")
                .build();
    }

    @Test
    void getAllFaqs_ShouldReturnListOfFaqs() {
        // Arrange
        List<Faq> faqs = Arrays.asList(testFaq);
        when(faqRepository.findAllByOrderByCreatedAtDesc()).thenReturn(faqs);

        // Act
        List<FaqResponse> result = faqService.getAllFaqs();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(faqRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getAllFaqsByProfileId_ShouldReturnListOfFaqs() {
        // Arrange
        List<Faq> faqs = Arrays.asList(testFaq);
        when(faqRepository.findAllByProfileIdOrderByCreatedAtDesc(1L)).thenReturn(faqs);

        // Act
        List<FaqResponse> result = faqService.getAllFaqsByProfileId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(faqRepository, times(1)).findAllByProfileIdOrderByCreatedAtDesc(1L);
    }

    @Test
    void getFaqById_WhenExists_ShouldReturnFaq() {
        // Arrange
        when(faqRepository.findById(1L)).thenReturn(Optional.of(testFaq));

        // Act
        FaqResponse result = faqService.getFaqById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testFaq.getQuestion(), result.getQuestion());
        verify(faqRepository, times(1)).findById(1L);
    }

    @Test
    void getFaqById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(faqRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            faqService.getFaqById(999L));
        verify(faqRepository, times(1)).findById(999L);
    }

    @Test
    void createFaq_WithValidData_ShouldReturnCreatedFaq() {
        // Arrange
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(faqRepository.save(any(Faq.class))).thenReturn(testFaq);

        // Act
        FaqResponse result = faqService.createFaq(testRequestInsert);

        // Assert
        assertNotNull(result);
        assertEquals(testFaq.getQuestion(), result.getQuestion());
        verify(profileRepository, times(1)).findById(1L);
        verify(faqRepository, times(1)).save(any(Faq.class));
    }

    @Test
    void createFaq_WithInvalidProfileId_ShouldThrowException() {
        // Arrange
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            faqService.createFaq(testRequestInsert));
        verify(profileRepository, times(1)).findById(1L);
        verify(faqRepository, never()).save(any(Faq.class));
    }

    @Test
    void updateFaq_WithValidData_ShouldReturnUpdatedFaq() {
        // Arrange
        when(faqRepository.findById(1L)).thenReturn(Optional.of(testFaq));
        when(faqRepository.save(any(Faq.class))).thenReturn(testFaq);

        // Act
        FaqResponse result = faqService.updateFaq(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(faqRepository, times(1)).findById(1L);
        verify(faqRepository, times(1)).save(any(Faq.class));
    }

    @Test
    void updateFaq_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(faqRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            faqService.updateFaq(999L, testRequestUpdate));
        verify(faqRepository, times(1)).findById(999L);
        verify(faqRepository, never()).save(any(Faq.class));
    }

    @Test
    void deleteFaq_WhenExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(faqRepository.existsById(1L)).thenReturn(true);
        doNothing().when(faqRepository).deleteById(1L);

        // Act
        faqService.deleteFaq(1L);

        // Assert
        verify(faqRepository, times(1)).existsById(1L);
        verify(faqRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteFaq_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(faqRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            faqService.deleteFaq(999L));
        verify(faqRepository, times(1)).existsById(999L);
        verify(faqRepository, never()).deleteById(anyLong());
    }
}


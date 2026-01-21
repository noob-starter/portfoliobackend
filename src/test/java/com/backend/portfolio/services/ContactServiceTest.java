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
import com.backend.portfolio.models.entities.Contact;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.ContactRequestInsert;
import com.backend.portfolio.models.responses.ContactResponse;
import com.backend.portfolio.models.updates.ContactRequestUpdate;
import com.backend.portfolio.repositories.ContactRepository;
import com.backend.portfolio.repositories.ProfileRepository;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ContactService contactService;

    private Profile testProfile;
    private Contact testContact;
    private ContactRequestInsert testRequestInsert;
    private ContactRequestUpdate testRequestUpdate;

    @BeforeEach
    void setUp() {
        testProfile = Profile.builder()
                .id(1L)
                .fname("John")
                .lname("Doe")
                .build();

        testContact = Contact.builder()
                .id(1L)
                .platform("LinkedIn")
                .url("http://linkedin.com/test")
                .description("Test Description")
                .banner("banner.jpg")
                .profile(testProfile)
                .build();

        testRequestInsert = ContactRequestInsert.builder()
                .platform("LinkedIn")
                .url("http://linkedin.com/test")
                .description("Test Description")
                .banner("banner.jpg")
                .profileId(1L)
                .build();

        testRequestUpdate = ContactRequestUpdate.builder()
                .platform("Twitter")
                .url("http://twitter.com/test")
                .build();
    }

    @Test
    void getAllContacts_ShouldReturnListOfContacts() {
        // Arrange
        List<Contact> contacts = Arrays.asList(testContact);
        when(contactRepository.findAllByOrderByCreatedAtDesc()).thenReturn(contacts);

        // Act
        List<ContactResponse> result = contactService.getAllContacts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(contactRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getAllContactsByProfileId_ShouldReturnListOfContacts() {
        // Arrange
        List<Contact> contacts = Arrays.asList(testContact);
        when(contactRepository.findAllByProfileIdOrderByCreatedAtDesc(1L)).thenReturn(contacts);

        // Act
        List<ContactResponse> result = contactService.getAllContactsByProfileId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(contactRepository, times(1)).findAllByProfileIdOrderByCreatedAtDesc(1L);
    }

    @Test
    void getContactById_WhenExists_ShouldReturnContact() {
        // Arrange
        when(contactRepository.findById(1L)).thenReturn(Optional.of(testContact));

        // Act
        ContactResponse result = contactService.getContactById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testContact.getPlatform(), result.getPlatform());
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    void getContactById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(contactRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            contactService.getContactById(999L));
        verify(contactRepository, times(1)).findById(999L);
    }

    @Test
    void createContact_WithValidData_ShouldReturnCreatedContact() {
        // Arrange
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(contactRepository.save(any(Contact.class))).thenReturn(testContact);

        // Act
        ContactResponse result = contactService.createContact(testRequestInsert);

        // Assert
        assertNotNull(result);
        assertEquals(testContact.getPlatform(), result.getPlatform());
        verify(profileRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    void createContact_WithInvalidProfileId_ShouldThrowException() {
        // Arrange
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            contactService.createContact(testRequestInsert));
        verify(profileRepository, times(1)).findById(1L);
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void updateContact_WithValidData_ShouldReturnUpdatedContact() {
        // Arrange
        when(contactRepository.findById(1L)).thenReturn(Optional.of(testContact));
        when(contactRepository.save(any(Contact.class))).thenReturn(testContact);

        // Act
        ContactResponse result = contactService.updateContact(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(contactRepository, times(1)).findById(1L);
        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    void updateContact_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(contactRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            contactService.updateContact(999L, testRequestUpdate));
        verify(contactRepository, times(1)).findById(999L);
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void deleteContact_WhenExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(contactRepository.existsById(1L)).thenReturn(true);
        doNothing().when(contactRepository).deleteById(1L);

        // Act
        contactService.deleteContact(1L);

        // Assert
        verify(contactRepository, times(1)).existsById(1L);
        verify(contactRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteContact_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(contactRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            contactService.deleteContact(999L));
        verify(contactRepository, times(1)).existsById(999L);
        verify(contactRepository, never()).deleteById(anyLong());
    }
}


package com.backend.portfolio.services;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Contact;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.ContactRequestInsert;
import com.backend.portfolio.models.responses.ContactResponse;
import com.backend.portfolio.models.updates.ContactRequestUpdate;
import com.backend.portfolio.repositories.ContactRepository;
import com.backend.portfolio.repositories.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactRepository contactRepository;
    private final ProfileRepository profileRepository;

    /**
     * Get all contacts ordered by creation date (descending)
     */
    @Transactional(readOnly = true)
    public List<ContactResponse> getAllContacts() {
        log.info("Fetching all contacts");
        return contactRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ContactResponse::fromContact)
                .collect(Collectors.toList());
    }

    /**
     * Get all contacts for a specific profile
     */
    @Transactional(readOnly = true)
    public List<ContactResponse> getAllContactsByProfileId(Long profileId) {
        log.info("Fetching all contacts for profile id: {}", profileId);
        return contactRepository.findAllByProfileIdOrderByCreatedAtDesc(profileId)
                .stream()
                .map(ContactResponse::fromContact)
                .collect(Collectors.toList());
    }

    /**
     * Get a single contact by ID
     */
    @Transactional(readOnly = true)
    public ContactResponse getContactById(Long id) {
        log.info("Fetching contact with id: {}", id);
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        return ContactResponse.fromContact(contact);
    }

    /**
     * Create a new contact
     */
    @Transactional
    public ContactResponse createContact(ContactRequestInsert request) {
        log.info("Creating new contact for profile id: {}", request.getProfileId());

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));

        Contact contact = Contact.builder()
                .platform(request.getPlatform())
                .url(request.getUrl())
                .description(request.getDescription())
                .banner(request.getBanner())
                .profile(profile)
                .build();

        Contact savedContact = contactRepository.save(contact);
        log.info("Contact created successfully with id: {}", savedContact.getId());

        return ContactResponse.fromContact(savedContact);
    }

    /**
     * Update an existing contact
     */
    @Transactional
    public ContactResponse updateContact(Long id, ContactRequestUpdate request) {
        log.info("Updating contact with id: {}", id);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));

        updateIfNotNull(request.getPlatform(), contact::setPlatform);
        updateIfNotNull(request.getUrl(), contact::setUrl);
        updateIfNotNull(request.getDescription(), contact::setDescription);
        updateIfNotNull(request.getBanner(), contact::setBanner);

        if (request.getProfileId() != null) {
            Profile profile = profileRepository.findById(request.getProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));
            contact.setProfile(profile);
        }

        Contact updatedContact = contactRepository.save(contact);
        log.info("Contact updated successfully with id: {}", updatedContact.getId());

        return ContactResponse.fromContact(updatedContact);
    }

    /**
     * Delete a contact by ID
     */
    @Transactional
    public void deleteContact(Long id) {
        log.info("Deleting contact with id: {}", id);

        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contact not found with id: " + id);
        }

        contactRepository.deleteById(id);
        log.info("Contact deleted successfully with id: {}", id);
    }

    /**
     * Helper method to update field only if value is not null
     */
    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}


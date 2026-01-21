package com.backend.portfolio.services;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Inquire;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.InquireRequestInsert;
import com.backend.portfolio.models.responses.InquireResponse;
import com.backend.portfolio.repositories.InquireRepository;
import com.backend.portfolio.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquireService {

    private final InquireRepository inquireRepository;
    private final ProfileRepository profileRepository;

    /**
     * Get all inquires ordered by creation date (descending)
     */
    @Transactional(readOnly = true)
    public List<InquireResponse> getAllInquires() {
        log.info("Fetching all inquires");
        return inquireRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(InquireResponse::fromInquire)
                .collect(Collectors.toList());
    }

    /**
     * Get all inquires for a specific profile
     */
    @Transactional(readOnly = true)
    public List<InquireResponse> getAllInquiresByProfileId(Long profileId) {
        log.info("Fetching all inquires for profile id: {}", profileId);
        return inquireRepository.findAllByProfileIdOrderByCreatedAtDesc(profileId)
                .stream()
                .map(InquireResponse::fromInquire)
                .collect(Collectors.toList());
    }

    /**
     * Get a single inquire by ID
     */
    @Transactional(readOnly = true)
    public InquireResponse getInquireById(Long id) {
        log.info("Fetching inquire with id: {}", id);
        Inquire inquire = inquireRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inquire not found with id: " + id));
        return InquireResponse.fromInquire(inquire);
    }

    /**
     * Create a new inquire
     */
    @Transactional
    public InquireResponse createInquire(InquireRequestInsert request) {
        log.info("Creating new inquire for profile id: {}", request.getProfileId());

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));

        Inquire inquire = Inquire.builder()
                .name(request.getName())
                .email(request.getEmail())
                .message(request.getMessage())
                .profile(profile)
                .build();

        Inquire savedInquire = inquireRepository.save(inquire);
        log.info("Inquire created successfully with id: {}", savedInquire.getId());

        return InquireResponse.fromInquire(savedInquire);
    }

    /**
     * Delete an inquire by ID
     */
    @Transactional
    public void deleteInquire(Long id) {
        log.info("Deleting inquire with id: {}", id);

        if (!inquireRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inquire not found with id: " + id);
        }

        inquireRepository.deleteById(id);
        log.info("Inquire deleted successfully with id: {}", id);
    }
}


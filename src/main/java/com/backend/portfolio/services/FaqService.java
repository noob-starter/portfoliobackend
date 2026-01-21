package com.backend.portfolio.services;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Faq;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.FaqRequestInsert;
import com.backend.portfolio.models.responses.FaqResponse;
import com.backend.portfolio.models.updates.FaqRequestUpdate;
import com.backend.portfolio.repositories.FaqRepository;
import com.backend.portfolio.repositories.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaqService {

    private final FaqRepository faqRepository;
    private final ProfileRepository profileRepository;

    /**
     * Get all FAQs ordered by creation date (descending)
     */
    @Transactional(readOnly = true)
    public List<FaqResponse> getAllFaqs() {
        log.info("Fetching all FAQs");
        return faqRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(FaqResponse::fromFaq)
                .collect(Collectors.toList());
    }

    /**
     * Get all FAQs for a specific profile
     */
    @Transactional(readOnly = true)
    public List<FaqResponse> getAllFaqsByProfileId(Long profileId) {
        log.info("Fetching all FAQs for profile id: {}", profileId);
        return faqRepository.findAllByProfileIdOrderByCreatedAtDesc(profileId)
                .stream()
                .map(FaqResponse::fromFaq)
                .collect(Collectors.toList());
    }

    /**
     * Get a single FAQ by ID
     */
    @Transactional(readOnly = true)
    public FaqResponse getFaqById(Long id) {
        log.info("Fetching FAQ with id: {}", id);
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ not found with id: " + id));
        return FaqResponse.fromFaq(faq);
    }

    /**
     * Create a new FAQ
     */
    @Transactional
    public FaqResponse createFaq(FaqRequestInsert request) {
        log.info("Creating new FAQ for profile id: {}", request.getProfileId());

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));

        Faq faq = Faq.builder()
                .question(request.getQuestion())
                .answer(request.getAnswer())
                .profile(profile)
                .build();

        Faq savedFaq = faqRepository.save(faq);
        log.info("FAQ created successfully with id: {}", savedFaq.getId());

        return FaqResponse.fromFaq(savedFaq);
    }

    /**
     * Update an existing FAQ
     */
    @Transactional
    public FaqResponse updateFaq(Long id, FaqRequestUpdate request) {
        log.info("Updating FAQ with id: {}", id);

        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ not found with id: " + id));

        updateIfNotNull(request.getQuestion(), faq::setQuestion);
        updateIfNotNull(request.getAnswer(), faq::setAnswer);

        if (request.getProfileId() != null) {
            Profile profile = profileRepository.findById(request.getProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));
            faq.setProfile(profile);
        }

        Faq updatedFaq = faqRepository.save(faq);
        log.info("FAQ updated successfully with id: {}", updatedFaq.getId());

        return FaqResponse.fromFaq(updatedFaq);
    }

    /**
     * Delete a FAQ by ID
     */
    @Transactional
    public void deleteFaq(Long id) {
        log.info("Deleting FAQ with id: {}", id);

        if (!faqRepository.existsById(id)) {
            throw new ResourceNotFoundException("FAQ not found with id: " + id);
        }

        faqRepository.deleteById(id);
        log.info("FAQ deleted successfully with id: {}", id);
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


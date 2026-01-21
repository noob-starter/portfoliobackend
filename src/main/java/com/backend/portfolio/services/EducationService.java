package com.backend.portfolio.services;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Education;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.EducationRequestInsert;
import com.backend.portfolio.models.responses.EducationResponse;
import com.backend.portfolio.models.updates.EducationRequestUpdate;
import com.backend.portfolio.repositories.EducationRepository;
import com.backend.portfolio.repositories.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationService {

    private final EducationRepository educationRepository;
    private final ProfileRepository profileRepository;

    /**
     * Get all educations across all profiles
     */
    @Transactional(readOnly = true)
    public List<EducationResponse> getAllEducations() {
        log.info("Fetching all educations");
        return educationRepository.findAll()
                .stream()
                .map(EducationResponse::fromEducation)
                .collect(Collectors.toList());
    }

    /**
     * Get education by ID
     */
    @Transactional(readOnly = true)
    public EducationResponse getEducationById(Long id) {
        log.info("Fetching education with id: {}", id);
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + id));
        return EducationResponse.fromEducation(education);
    }

    /**
     * Get all educations for a specific profile
     */
    @Transactional(readOnly = true)
    public List<EducationResponse> getAllEducationsByProfileId(Long profileId) {
        log.info("Fetching all educations for profile id: {}", profileId);
        return educationRepository.findAllByProfileIdOrderByStartDateDesc(profileId)
                .stream()
                .map(EducationResponse::fromEducation)
                .collect(Collectors.toList());
    }

    /**
     * Create a new education
     */
    @Transactional
    public EducationResponse createEducation(EducationRequestInsert request) {
        log.info("Creating new education for profile id: {}", request.getProfileId());

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));

        Education education = Education.builder()
                .degree(request.getDegree())
                .institution(request.getInstitution())
                .field(request.getField())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .percentage(request.getPercentage())
                .description(request.getDescription())
                .url(request.getUrl())
                .banner(request.getBanner())
                .github(request.getGithub())
                .profile(profile)
                .build();

        Education savedEducation = educationRepository.save(education);
        log.info("Education created successfully with id: {}", savedEducation.getId());

        return EducationResponse.fromEducation(savedEducation);
    }

    /**
     * Update an existing education
     */
    @Transactional
    public EducationResponse updateEducation(Long id, EducationRequestUpdate request) {
        log.info("Updating education with id: {}", id);

        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + id));

        updateIfNotNull(request.getDegree(), education::setDegree);
        updateIfNotNull(request.getInstitution(), education::setInstitution);
        updateIfNotNull(request.getField(), education::setField);
        updateIfNotNull(request.getStartDate(), education::setStartDate);
        updateIfNotNull(request.getEndDate(), education::setEndDate);
        updateIfNotNull(request.getPercentage(), education::setPercentage);
        updateIfNotNull(request.getDescription(), education::setDescription);
        updateIfNotNull(request.getUrl(), education::setUrl);
        updateIfNotNull(request.getBanner(), education::setBanner);
        updateIfNotNull(request.getGithub(), education::setGithub);

        if (request.getProfileId() != null) {
            Profile profile = profileRepository.findById(request.getProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));
            education.setProfile(profile);
        }

        Education updatedEducation = educationRepository.save(education);
        log.info("Education updated successfully with id: {}", updatedEducation.getId());

        return EducationResponse.fromEducation(updatedEducation);
    }

    /**
     * Delete an education by ID
     */
    @Transactional
    public void deleteEducation(Long id) {
        log.info("Deleting education with id: {}", id);

        if (!educationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Education not found with id: " + id);
        }

        educationRepository.deleteById(id);
        log.info("Education deleted successfully with id: {}", id);
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


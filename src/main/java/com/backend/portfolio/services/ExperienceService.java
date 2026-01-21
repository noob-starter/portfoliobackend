package com.backend.portfolio.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Experience;
import com.backend.portfolio.models.entities.ExperiencePoint;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.entities.Technology;
import com.backend.portfolio.models.requests.ExperienceRequestInsert;
import com.backend.portfolio.models.responses.ExperienceResponse;
import com.backend.portfolio.models.updates.ExperienceRequestUpdate;
import com.backend.portfolio.repositories.ExperienceRepository;
import com.backend.portfolio.repositories.ProfileRepository;
import com.backend.portfolio.repositories.TechnologyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final ProfileRepository profileRepository;
    private final TechnologyRepository technologyRepository;

    /**
     * Get all experiences ordered by creation date (descending)
     */
    @Transactional(readOnly = true)
    public List<ExperienceResponse> getAllExperiences() {
        log.info("Fetching all experiences");
        List<Experience> experiences = experienceRepository.findAllByOrderByCreatedAtDesc();
        return experiences.stream()
                .map(ExperienceResponse::fromExperience)
                .collect(Collectors.toList());
    }

    /**
     * Get all experiences for a specific profile
     */
    @Transactional(readOnly = true)
    public List<ExperienceResponse> getAllExperiencesByProfileId(Long profileId) {
        log.info("Fetching all experiences for profile id: {}", profileId);
        List<Experience> experiences = experienceRepository.findAllByProfileIdOrderByStartDateDesc(profileId);
        return experiences.stream()
                .map(ExperienceResponse::fromExperience)
                .collect(Collectors.toList());
    }

    /**
     * Get a single experience by ID
     */
    @Transactional(readOnly = true)
    public ExperienceResponse getExperienceById(Long id) {
        log.info("Fetching experience with id: {}", id);
        Experience experience = experienceRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + id));
        return ExperienceResponse.fromExperience(experience);
    }

    /**
     * Create a new experience
     */
    @Transactional
    public ExperienceResponse createExperience(ExperienceRequestInsert request) {
        log.info("Creating new experience for profile id: {}", request.getProfileId());

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));

        Experience experience = Experience.builder()
                .company(request.getCompany())
                .position(request.getPosition())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .location(request.getLocation())
                .url(request.getUrl())
                .banner(request.getBanner())
                .github(request.getGithub())
                .profile(profile)
                .technologies(new HashSet<>())
                .experiencePoints(new HashSet<>())
                .build();

        // Add technologies if provided
        if (request.getTechnologyIds() != null && !request.getTechnologyIds().isEmpty()) {
            Set<Technology> technologies = request.getTechnologyIds().stream()
                    .map(id -> technologyRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id: " + id)))
                    .collect(Collectors.toSet());
            experience.setTechnologies(technologies);
        }

        Experience savedExperience = experienceRepository.save(experience);

        // Add experience points if provided
        if (request.getExperiencePoints() != null && !request.getExperiencePoints().isEmpty()) {
            final Experience finalExperience = savedExperience;
            Set<ExperiencePoint> points = request.getExperiencePoints().stream()
                    .map(content -> ExperiencePoint.builder()
                            .content(content)
                            .experience(finalExperience)
                            .build())
                    .collect(Collectors.toSet());
            savedExperience.getExperiencePoints().addAll(points);
            savedExperience = experienceRepository.save(savedExperience);
        }

        log.info("Experience created successfully with id: {}", savedExperience.getId());

        return ExperienceResponse.fromExperience(savedExperience);
    }

    /**
     * Update an existing experience
     */
    @Transactional
    public ExperienceResponse updateExperience(Long id, ExperienceRequestUpdate request) {
        log.info("Updating experience with id: {}", id);

        Experience experience = experienceRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + id));

        updateIfNotNull(request.getCompany(), experience::setCompany);
        updateIfNotNull(request.getPosition(), experience::setPosition);
        updateIfNotNull(request.getStartDate(), experience::setStartDate);
        updateIfNotNull(request.getEndDate(), experience::setEndDate);
        updateIfNotNull(request.getLocation(), experience::setLocation);
        updateIfNotNull(request.getUrl(), experience::setUrl);
        updateIfNotNull(request.getBanner(), experience::setBanner);
        updateIfNotNull(request.getGithub(), experience::setGithub);

        if (request.getProfileId() != null) {
            Profile profile = profileRepository.findById(request.getProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));
            experience.setProfile(profile);
        }

        // Update technologies if provided
        if (request.getTechnologyIds() != null) {
            Set<Technology> technologies = request.getTechnologyIds().stream()
                    .map(techId -> technologyRepository.findById(techId)
                            .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id: " + techId)))
                    .collect(Collectors.toSet());
            experience.getTechnologies().clear();
            experience.getTechnologies().addAll(technologies);
        }

        Experience updatedExperience = experienceRepository.save(experience);
        log.info("Experience updated successfully with id: {}", updatedExperience.getId());

        return ExperienceResponse.fromExperience(updatedExperience);
    }

    /**
     * Delete an experience by ID
     */
    @Transactional
    public void deleteExperience(Long id) {
        log.info("Deleting experience with id: {}", id);

        if (!experienceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Experience not found with id: " + id);
        }

        experienceRepository.deleteById(id);
        log.info("Experience deleted successfully with id: {}", id);
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


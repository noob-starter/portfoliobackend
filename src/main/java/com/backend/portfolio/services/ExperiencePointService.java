package com.backend.portfolio.services;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Experience;
import com.backend.portfolio.models.entities.ExperiencePoint;
import com.backend.portfolio.models.requests.ExperiencePointRequestInsert;
import com.backend.portfolio.models.responses.ExperiencePointResponse;
import com.backend.portfolio.models.updates.ExperiencePointRequestUpdate;
import com.backend.portfolio.repositories.ExperiencePointRepository;
import com.backend.portfolio.repositories.ExperienceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExperiencePointService {

    private final ExperiencePointRepository experiencePointRepository;
    private final ExperienceRepository experienceRepository;

    /**
     * Get all experience points for a specific experience
     */
    @Transactional(readOnly = true)
    public List<ExperiencePointResponse> getAllExperiencePointsByExperienceId(Long experienceId) {
        log.info("Fetching all experience points for experience id: {}", experienceId);
        return experiencePointRepository.findAllByExperienceIdOrderByCreatedAtAsc(experienceId)
                .stream()
                .map(ExperiencePointResponse::fromExperiencePoint)
                .collect(Collectors.toList());
    }

    /**
     * Get a single experience point by ID
     */
    @Transactional(readOnly = true)
    public ExperiencePointResponse getExperiencePointById(Long id) {
        log.info("Fetching experience point with id: {}", id);
        ExperiencePoint experiencePoint = experiencePointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience point not found with id: " + id));
        return ExperiencePointResponse.fromExperiencePoint(experiencePoint);
    }

    /**
     * Create a new experience point
     */
    @Transactional
    public ExperiencePointResponse createExperiencePoint(ExperiencePointRequestInsert request) {
        log.info("Creating new experience point for experience id: {}", request.getExperienceId());

        Experience experience = experienceRepository.findById(request.getExperienceId())
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + request.getExperienceId()));

        ExperiencePoint experiencePoint = ExperiencePoint.builder()
                .content(request.getContent())
                .experience(experience)
                .build();

        ExperiencePoint savedExperiencePoint = experiencePointRepository.save(experiencePoint);
        log.info("Experience point created successfully with id: {}", savedExperiencePoint.getId());

        return ExperiencePointResponse.fromExperiencePoint(savedExperiencePoint);
    }

    /**
     * Update an existing experience point
     */
    @Transactional
    public ExperiencePointResponse updateExperiencePoint(Long id, ExperiencePointRequestUpdate request) {
        log.info("Updating experience point with id: {}", id);

        ExperiencePoint experiencePoint = experiencePointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience point not found with id: " + id));

        updateIfNotNull(request.getContent(), experiencePoint::setContent);

        if (request.getExperienceId() != null) {
            Experience experience = experienceRepository.findById(request.getExperienceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + request.getExperienceId()));
            experiencePoint.setExperience(experience);
        }

        ExperiencePoint updatedExperiencePoint = experiencePointRepository.save(experiencePoint);
        log.info("Experience point updated successfully with id: {}", updatedExperiencePoint.getId());

        return ExperiencePointResponse.fromExperiencePoint(updatedExperiencePoint);
    }

    /**
     * Delete an experience point by ID
     */
    @Transactional
    public void deleteExperiencePoint(Long id) {
        log.info("Deleting experience point with id: {}", id);

        if (!experiencePointRepository.existsById(id)) {
            throw new ResourceNotFoundException("Experience point not found with id: " + id);
        }

        experiencePointRepository.deleteById(id);
        log.info("Experience point deleted successfully with id: {}", id);
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


package com.backend.portfolio.services;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Technology;
import com.backend.portfolio.models.requests.TechnologyRequestInsert;
import com.backend.portfolio.models.responses.TechnologyResponse;
import com.backend.portfolio.models.updates.TechnologyRequestUpdate;
import com.backend.portfolio.repositories.TechnologyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    /**
     * Get all technologies ordered by creation date (descending)
     */
    @Transactional(readOnly = true)
    public List<TechnologyResponse> getAllTechnologies() {
        log.info("Fetching all technologies");
        return technologyRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(TechnologyResponse::fromTechnology)
                .collect(Collectors.toList());
    }

    /**
     * Get all technologies by category
     */
    @Transactional(readOnly = true)
    public List<TechnologyResponse> getAllTechnologiesByCategory(String category) {
        log.info("Fetching all technologies for category: {}", category);
        return technologyRepository.findAllByCategoryOrderByNameAsc(category)
                .stream()
                .map(TechnologyResponse::fromTechnology)
                .collect(Collectors.toList());
    }

    /**
     * Get a single technology by ID
     */
    @Transactional(readOnly = true)
    public TechnologyResponse getTechnologyById(Long id) {
        log.info("Fetching technology with id: {}", id);
        Technology technology = technologyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id: " + id));
        return TechnologyResponse.fromTechnology(technology);
    }

    /**
     * Get a single technology by name
     */
    @Transactional(readOnly = true)
    public TechnologyResponse getTechnologyByName(String name) {
        log.info("Fetching technology with name: {}", name);
        Technology technology = technologyRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Technology not found with name: " + name));
        return TechnologyResponse.fromTechnology(technology);
    }

    /**
     * Get all technologies for a specific profile
     */
    @Transactional(readOnly = true)
    public List<TechnologyResponse> getAllTechnologiesByProfileId(Long profileId) {
        log.info("Fetching all technologies for profile id: {}", profileId);
        return technologyRepository.findAllByProfileIdOrderByCategoryAndName(profileId)
                .stream()
                .map(TechnologyResponse::fromTechnology)
                .collect(Collectors.toList());
    }

    /**
     * Create a new technology
     */
    @Transactional
    public TechnologyResponse createTechnology(TechnologyRequestInsert request) {
        log.info("Creating new technology: {}", request.getName());

        Technology technology = Technology.builder()
                .name(request.getName())
                .category(request.getCategory())
                .type(request.getType())
                .proficiency(request.getProficiency())
                .banner(request.getBanner())
                .github(request.getGithub())
                .build();

        Technology savedTechnology = technologyRepository.save(technology);
        log.info("Technology created successfully with id: {}", savedTechnology.getId());

        return TechnologyResponse.fromTechnology(savedTechnology);
    }

    /**
     * Update an existing technology
     */
    @Transactional
    public TechnologyResponse updateTechnology(Long id, TechnologyRequestUpdate request) {
        log.info("Updating technology with id: {}", id);

        Technology technology = technologyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technology not found with id: " + id));

        updateIfNotNull(request.getName(), technology::setName);
        updateIfNotNull(request.getCategory(), technology::setCategory);
        updateIfNotNull(request.getType(), technology::setType);
        updateIfNotNull(request.getProficiency(), technology::setProficiency);
        updateIfNotNull(request.getBanner(), technology::setBanner);
        updateIfNotNull(request.getGithub(), technology::setGithub);

        Technology updatedTechnology = technologyRepository.save(technology);
        log.info("Technology updated successfully with id: {}", updatedTechnology.getId());

        return TechnologyResponse.fromTechnology(updatedTechnology);
    }

    /**
     * Delete a technology by ID
     */
    @Transactional
    public void deleteTechnology(Long id) {
        log.info("Deleting technology with id: {}", id);

        if (!technologyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Technology not found with id: " + id);
        }

        technologyRepository.deleteById(id);
        log.info("Technology deleted successfully with id: {}", id);
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


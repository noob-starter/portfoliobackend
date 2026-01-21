package com.backend.portfolio.services;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Achievement;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.AchievementRequestInsert;
import com.backend.portfolio.models.responses.AchievementResponse;
import com.backend.portfolio.models.updates.AchievementRequestUpdate;
import com.backend.portfolio.repositories.AchievementRepository;
import com.backend.portfolio.repositories.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final ProfileRepository profileRepository;

    /**
     * Get all achievements ordered by creation date (descending)
     */
    @Transactional(readOnly = true)
    public List<AchievementResponse> getAllAchievements() {
        log.info("Fetching all achievements");
        return achievementRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(AchievementResponse::fromAchievement)
                .collect(Collectors.toList());
    }

    /**
     * Get all achievements for a specific profile
     */
    @Transactional(readOnly = true)
    public List<AchievementResponse> getAllAchievementsByProfileId(Long profileId) {
        log.info("Fetching all achievements for profile id: {}", profileId);
        return achievementRepository.findAllByProfileIdOrderByDateAchievedDesc(profileId)
                .stream()
                .map(AchievementResponse::fromAchievement)
                .collect(Collectors.toList());
    }

    /**
     * Get a single achievement by ID
     */
    @Transactional(readOnly = true)
    public AchievementResponse getAchievementById(Long id) {
        log.info("Fetching achievement with id: {}", id);
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found with id: " + id));
        return AchievementResponse.fromAchievement(achievement);
    }

    /**
     * Create a new achievement
     */
    @Transactional
    public AchievementResponse createAchievement(AchievementRequestInsert request) {
        log.info("Creating new achievement for profile id: {}", request.getProfileId());

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));

        Achievement achievement = Achievement.builder()
                .name(request.getName())
                .dateAchieved(request.getDateAchieved())
                .issuer(request.getIssuer())
                .description(request.getDescription())
                .url(request.getUrl())
                .banner(request.getBanner())
                .github(request.getGithub())
                .profile(profile)
                .build();

        Achievement savedAchievement = achievementRepository.save(achievement);
        log.info("Achievement created successfully with id: {}", savedAchievement.getId());

        return AchievementResponse.fromAchievement(savedAchievement);
    }

    /**
     * Update an existing achievement
     */
    @Transactional
    public AchievementResponse updateAchievement(Long id, AchievementRequestUpdate request) {
        log.info("Updating achievement with id: {}", id);

        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found with id: " + id));

        updateIfNotNull(request.getName(), achievement::setName);
        updateIfNotNull(request.getDateAchieved(), achievement::setDateAchieved);
        updateIfNotNull(request.getIssuer(), achievement::setIssuer);
        updateIfNotNull(request.getDescription(), achievement::setDescription);
        updateIfNotNull(request.getUrl(), achievement::setUrl);
        updateIfNotNull(request.getBanner(), achievement::setBanner);
        updateIfNotNull(request.getGithub(), achievement::setGithub);

        if (request.getProfileId() != null) {
            Profile profile = profileRepository.findById(request.getProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));
            achievement.setProfile(profile);
        }

        Achievement updatedAchievement = achievementRepository.save(achievement);
        log.info("Achievement updated successfully with id: {}", updatedAchievement.getId());

        return AchievementResponse.fromAchievement(updatedAchievement);
    }

    /**
     * Delete an achievement by ID
     */
    @Transactional
    public void deleteAchievement(Long id) {
        log.info("Deleting achievement with id: {}", id);

        if (!achievementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Achievement not found with id: " + id);
        }

        achievementRepository.deleteById(id);
        log.info("Achievement deleted successfully with id: {}", id);
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


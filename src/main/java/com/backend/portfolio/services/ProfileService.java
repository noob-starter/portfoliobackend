package com.backend.portfolio.services;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.ProfileRequestInsert;
import com.backend.portfolio.models.responses.ProfileResponse;
import com.backend.portfolio.models.updates.ProfileRequestResponse;
import com.backend.portfolio.repositories.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private final ProfileRepository profileRepository;

    /**
     * Get all profiles ordered by creation date (descending)
     */
    @Transactional(readOnly = true)
    public List<ProfileResponse> getAllProfiles() {
        log.info("Fetching all profiles");
        return profileRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ProfileResponse::fromProfile)
                .collect(Collectors.toList());
    }

    /**
     * Get a single regular by ID
     */
    @Transactional(readOnly = true)
    public ProfileResponse getProfileById(Long id) {
        log.info("Fetching regular with id: {}", id);
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));
        return ProfileResponse.fromProfile(profile);
    }

    /**
     * Get a single regular by first name and last name
     */
    @Transactional(readOnly = true)
    public ProfileResponse getProfileByName(String fname, String lname) {
        log.info("Fetching regular with name: {} {}", fname, lname);
        Profile profile = profileRepository.findByFnameAndLname(fname, lname)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profile not found with name: " + fname + " " + lname));
        return ProfileResponse.fromProfile(profile);
    }

    /**
     * Create a new regular
     */
    @Transactional
    public ProfileResponse createProfile(ProfileRequestInsert request) {
        log.info("Creating new regular for: {} {}", request.getFname(), request.getLname());
        
        Profile profile = Profile.builder()
                .fname(request.getFname())
                .lname(request.getLname())
                .sex(request.getSex())
                .bio(request.getBio())
                .banner(request.getBanner())
                .intro(request.getIntro())
                .contour(request.getContour())
                .url(request.getUrl())
                .build();

        Profile savedProfile = profileRepository.save(profile);
        log.info("Profile created successfully with id: {}", savedProfile.getId());
        
        return ProfileResponse.fromProfile(savedProfile);
    }

    /**
     * Update an existing regular
     */
    @Transactional
    public ProfileResponse updateProfile(Long id, ProfileRequestResponse request) {
        log.info("Updating regular with id: {}", id);
        
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));

        updateIfNotNull(request.getFname(), profile::setFname);
        updateIfNotNull(request.getLname(), profile::setLname);
        updateIfNotNull(request.getSex(), profile::setSex);
        updateIfNotNull(request.getBio(), profile::setBio);
        updateIfNotNull(request.getBanner(), profile::setBanner);
        updateIfNotNull(request.getIntro(), profile::setIntro);
        updateIfNotNull(request.getContour(), profile::setContour);
        updateIfNotNull(request.getUrl(), profile::setUrl);

        Profile updatedProfile = profileRepository.save(profile);
        log.info("Profile updated successfully with id: {}", updatedProfile.getId());
        
        return ProfileResponse.fromProfile(updatedProfile);
    }

    /**
     * Delete a regular by ID
     */
    @Transactional
    public void deleteProfile(Long id) {
        log.info("Deleting regular with id: {}", id);
        
        if (!profileRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profile not found with id: " + id);
        }

        profileRepository.deleteById(id);
        log.info("Profile deleted successfully with id: {}", id);
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


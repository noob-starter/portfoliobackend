package com.backend.portfolio.services;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Address;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.AddressRequestInsert;
import com.backend.portfolio.models.responses.AddressResponse;
import com.backend.portfolio.models.updates.AddressRequestUpdate;
import com.backend.portfolio.repositories.AddressRepository;
import com.backend.portfolio.repositories.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final ProfileRepository profileRepository;

    /**
     * Get all addresses ordered by creation date (descending)
     */
    @Transactional(readOnly = true)
    public List<AddressResponse> getAllAddresses() {
        log.info("Fetching all addresses");
        return addressRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(AddressResponse::fromAddress)
                .collect(Collectors.toList());
    }

    /**
     * Get all addresses for a specific profile
     */
    @Transactional(readOnly = true)
    public List<AddressResponse> getAllAddressesByProfileId(Long profileId) {
        log.info("Fetching all addresses for profile id: {}", profileId);
        return addressRepository.findAllByProfileIdOrderByCreatedAtDesc(profileId)
                .stream()
                .map(AddressResponse::fromAddress)
                .collect(Collectors.toList());
    }

    /**
     * Get a single address by ID
     */
    @Transactional(readOnly = true)
    public AddressResponse getAddressById(Long id) {
        log.info("Fetching address with id: {}", id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        return AddressResponse.fromAddress(address);
    }

    /**
     * Create a new address
     */
    @Transactional
    public AddressResponse createAddress(AddressRequestInsert request) {
        log.info("Creating new address for profile id: {}", request.getProfileId());

        Profile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));

        Address address = Address.builder()
                .street(request.getStreet())
                .landmark(request.getLandmark())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .pincode(request.getPincode())
                .type(request.getType())
                .phone(request.getPhone())
                .email(request.getEmail())
                .url(request.getUrl())
                .profile(profile)
                .build();

        Address savedAddress = addressRepository.save(address);
        log.info("Address created successfully with id: {}", savedAddress.getId());

        return AddressResponse.fromAddress(savedAddress);
    }

    /**
     * Update an existing address
     */
    @Transactional
    public AddressResponse updateAddress(Long id, AddressRequestUpdate request) {
        log.info("Updating address with id: {}", id);

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        updateIfNotNull(request.getStreet(), address::setStreet);
        updateIfNotNull(request.getLandmark(), address::setLandmark);
        updateIfNotNull(request.getCity(), address::setCity);
        updateIfNotNull(request.getState(), address::setState);
        updateIfNotNull(request.getCountry(), address::setCountry);
        updateIfNotNull(request.getPincode(), address::setPincode);
        updateIfNotNull(request.getType(), address::setType);
        updateIfNotNull(request.getPhone(), address::setPhone);
        updateIfNotNull(request.getEmail(), address::setEmail);
        updateIfNotNull(request.getUrl(), address::setUrl);

        if (request.getProfileId() != null) {
            Profile profile = profileRepository.findById(request.getProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + request.getProfileId()));
            address.setProfile(profile);
        }

        Address updatedAddress = addressRepository.save(address);
        log.info("Address updated successfully with id: {}", updatedAddress.getId());

        return AddressResponse.fromAddress(updatedAddress);
    }

    /**
     * Delete an address by ID
     */
    @Transactional
    public void deleteAddress(Long id) {
        log.info("Deleting address with id: {}", id);

        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Address not found with id: " + id);
        }

        addressRepository.deleteById(id);
        log.info("Address deleted successfully with id: {}", id);
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


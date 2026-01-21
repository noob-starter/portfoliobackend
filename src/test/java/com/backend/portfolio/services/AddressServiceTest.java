package com.backend.portfolio.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.portfolio.exceptions.custom.ResourceNotFoundException;
import com.backend.portfolio.models.entities.Address;
import com.backend.portfolio.models.entities.Profile;
import com.backend.portfolio.models.requests.AddressRequestInsert;
import com.backend.portfolio.models.responses.AddressResponse;
import com.backend.portfolio.models.updates.AddressRequestUpdate;
import com.backend.portfolio.repositories.AddressRepository;
import com.backend.portfolio.repositories.ProfileRepository;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private AddressService addressService;

    private Profile testProfile;
    private Address testAddress;
    private AddressRequestInsert testRequestInsert;
    private AddressRequestUpdate testRequestUpdate;

    @BeforeEach
    void setUp() {
        testProfile = Profile.builder()
                .id(1L)
                .fname("John")
                .lname("Doe")
                .build();

        testAddress = Address.builder()
                .id(1L)
                .street("123 Main St")
                .landmark("Near Central Park")
                .city("New York")
                .state("NY")
                .country("USA")
                .pincode(10001)
                .type("Home")
                .phone("+1234567890")
                .email("john@example.com")
                .url("http://example.com")
                .profile(testProfile)
                .build();

        testRequestInsert = AddressRequestInsert.builder()
                .street("123 Main St")
                .landmark("Near Central Park")
                .city("New York")
                .state("NY")
                .country("USA")
                .pincode(10001)
                .type("Home")
                .phone("+1234567890")
                .email("john@example.com")
                .url("http://example.com")
                .profileId(1L)
                .build();

        testRequestUpdate = AddressRequestUpdate.builder()
                .street("456 Oak Ave")
                .city("Boston")
                .build();
    }

    @Test
    void getAllAddresses_ShouldReturnListOfAddresses() {
        // Arrange
        List<Address> addresses = Arrays.asList(testAddress);
        when(addressRepository.findAllByOrderByCreatedAtDesc()).thenReturn(addresses);

        // Act
        List<AddressResponse> result = addressService.getAllAddresses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(addressRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getAllAddressesByProfileId_ShouldReturnListOfAddresses() {
        // Arrange
        List<Address> addresses = Arrays.asList(testAddress);
        when(addressRepository.findAllByProfileIdOrderByCreatedAtDesc(1L)).thenReturn(addresses);

        // Act
        List<AddressResponse> result = addressService.getAllAddressesByProfileId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(addressRepository, times(1)).findAllByProfileIdOrderByCreatedAtDesc(1L);
    }

    @Test
    void getAddressById_WhenExists_ShouldReturnAddress() {
        // Arrange
        when(addressRepository.findById(1L)).thenReturn(Optional.of(testAddress));

        // Act
        AddressResponse result = addressService.getAddressById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testAddress.getStreet(), result.getStreet());
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void getAddressById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            addressService.getAddressById(999L));
        verify(addressRepository, times(1)).findById(999L);
    }

    @Test
    void createAddress_WithValidData_ShouldReturnCreatedAddress() {
        // Arrange
        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);

        // Act
        AddressResponse result = addressService.createAddress(testRequestInsert);

        // Assert
        assertNotNull(result);
        assertEquals(testAddress.getStreet(), result.getStreet());
        verify(profileRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void createAddress_WithInvalidProfileId_ShouldThrowException() {
        // Arrange
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            addressService.createAddress(testRequestInsert));
        verify(profileRepository, times(1)).findById(1L);
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void updateAddress_WithValidData_ShouldReturnUpdatedAddress() {
        // Arrange
        when(addressRepository.findById(1L)).thenReturn(Optional.of(testAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);

        // Act
        AddressResponse result = addressService.updateAddress(1L, testRequestUpdate);

        // Assert
        assertNotNull(result);
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void updateAddress_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            addressService.updateAddress(999L, testRequestUpdate));
        verify(addressRepository, times(1)).findById(999L);
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void deleteAddress_WhenExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(addressRepository.existsById(1L)).thenReturn(true);
        doNothing().when(addressRepository).deleteById(1L);

        // Act
        addressService.deleteAddress(1L);

        // Assert
        verify(addressRepository, times(1)).existsById(1L);
        verify(addressRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAddress_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(addressRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            addressService.deleteAddress(999L));
        verify(addressRepository, times(1)).existsById(999L);
        verify(addressRepository, never()).deleteById(anyLong());
    }
}


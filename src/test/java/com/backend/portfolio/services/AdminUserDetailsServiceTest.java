package com.backend.portfolio.services;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.backend.portfolio.models.entities.Admin;
import com.backend.portfolio.repositories.AdminRepository;

@ExtendWith(MockitoExtension.class)
class AdminUserDetailsServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminUserDetailsService adminUserDetailsService;

    private Admin testAdmin;

    @BeforeEach
    void setUp() {
        testAdmin = Admin.builder()
                .id(1L)
                .username("admin")
                .password("$2a$10$encodedPassword")
                .email("admin@test.com")
                .role("ADMIN")
                .enabled(true)
                .lastLogin(LocalDateTime.now())
                .build();
    }

    @Test
    void loadUserByUsername_WithValidUsername_ShouldReturnUserDetails() {
        // Arrange
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(testAdmin));
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

        // Act
        UserDetails result = adminUserDetailsService.loadUserByUsername("admin");

        // Assert
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertEquals("$2a$10$encodedPassword", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(result.isEnabled());
        verify(adminRepository, times(1)).findByUsername("admin");
        verify(adminRepository, times(1)).save(any(Admin.class));
    }

    @Test
    void loadUserByUsername_WithInvalidUsername_ShouldThrowException() {
        // Arrange
        when(adminRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> 
            adminUserDetailsService.loadUserByUsername("unknown"));
        verify(adminRepository, times(1)).findByUsername("unknown");
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void loadUserByUsername_WithDisabledAccount_ShouldThrowException() {
        // Arrange
        testAdmin.setEnabled(false);
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(testAdmin));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> 
            adminUserDetailsService.loadUserByUsername("admin"));
        verify(adminRepository, times(1)).findByUsername("admin");
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void loadUserByUsername_ShouldUpdateLastLoginTimestamp() {
        // Arrange
        LocalDateTime beforeLogin = LocalDateTime.now().minusMinutes(5);
        testAdmin.setLastLogin(beforeLogin);
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(testAdmin));
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

        // Act
        UserDetails result = adminUserDetailsService.loadUserByUsername("admin");

        // Assert
        assertNotNull(result);
        verify(adminRepository, times(1)).save(any(Admin.class));
    }
}


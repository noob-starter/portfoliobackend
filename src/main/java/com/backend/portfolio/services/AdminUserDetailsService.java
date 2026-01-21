package com.backend.portfolio.services;

import com.backend.portfolio.models.entities.Admin;
import com.backend.portfolio.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user details for username: {}", username);

        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Admin not found with username: {}", username);
                    return new UsernameNotFoundException("Admin not found with username: " + username);
                });

        if (!admin.getEnabled()) {
            log.warn("Admin account is disabled: {}", username);
            throw new UsernameNotFoundException("Admin account is disabled: " + username);
        }

        // Update last login timestamp
        admin.setLastLogin(LocalDateTime.now());
        adminRepository.save(admin);
        log.info("Last login updated for admin: {} at {}", username, admin.getLastLogin());

        log.debug("Successfully loaded user details for username: {}", username);

        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + admin.getRole())))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!admin.getEnabled())
                .build();
    }
}


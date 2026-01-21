package com.backend.portfolio.repositories;

import com.backend.portfolio.models.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByFnameAndLname(String fname, String lname);

    List<Profile> findAllByOrderByCreatedAtDesc();
}


package com.backend.portfolio.repositories;

import com.backend.portfolio.models.entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    @Query("SELECT DISTINCT e FROM Experience e LEFT JOIN FETCH e.experiencePoints LEFT JOIN FETCH e.technologies WHERE e.profile.id = :profileId ORDER BY e.startDate DESC")
    List<Experience> findAllByProfileIdOrderByStartDateDesc(Long profileId);

    @Query("SELECT DISTINCT e FROM Experience e LEFT JOIN FETCH e.experiencePoints LEFT JOIN FETCH e.technologies ORDER BY e.createdAt DESC")
    List<Experience> findAllByOrderByCreatedAtDesc();

    @Query("SELECT DISTINCT e FROM Experience e LEFT JOIN FETCH e.experiencePoints LEFT JOIN FETCH e.technologies WHERE e.id = :id")
    Optional<Experience> findByIdWithDetails(Long id);
}


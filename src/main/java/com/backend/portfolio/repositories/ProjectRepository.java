package com.backend.portfolio.repositories;

import com.backend.portfolio.models.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.projectPoints LEFT JOIN FETCH p.technologies WHERE p.profile.id = :profileId ORDER BY p.startDate DESC")
    List<Project> findAllByProfileIdOrderByStartDateDesc(Long profileId);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.projectPoints LEFT JOIN FETCH p.technologies ORDER BY p.createdAt DESC")
    List<Project> findAllByOrderByCreatedAtDesc();

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.projectPoints LEFT JOIN FETCH p.technologies WHERE p.id = :id")
    Optional<Project> findByIdWithDetails(Long id);
}


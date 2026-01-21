package com.backend.portfolio.repositories;

import com.backend.portfolio.models.entities.ExperiencePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperiencePointRepository extends JpaRepository<ExperiencePoint, Long> {

    List<ExperiencePoint> findAllByExperienceIdOrderByCreatedAtAsc(Long experienceId);
}


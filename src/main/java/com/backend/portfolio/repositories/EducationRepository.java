package com.backend.portfolio.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portfolio.models.entities.Education;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {

    List<Education> findAllByProfileIdOrderByStartDateDesc(Long profileId);
}


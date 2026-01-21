package com.backend.portfolio.repositories;

import com.backend.portfolio.models.entities.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    List<Achievement> findAllByProfileIdOrderByDateAchievedDesc(Long profileId);

    List<Achievement> findAllByOrderByCreatedAtDesc();
}


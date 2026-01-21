package com.backend.portfolio.repositories;

import com.backend.portfolio.models.entities.ProjectPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPointRepository extends JpaRepository<ProjectPoint, Long> {

    List<ProjectPoint> findAllByProjectIdOrderByCreatedAtAsc(Long projectId);
}


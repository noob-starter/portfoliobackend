package com.backend.portfolio.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.portfolio.models.entities.Technology;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {

    Optional<Technology> findByName(String name);

    List<Technology> findAllByOrderByCreatedAtDesc();

    List<Technology> findAllByCategoryOrderByNameAsc(String category);

    @Query("SELECT t FROM Technology t JOIN t.profiles p WHERE p.id = :profileId ORDER BY t.category ASC, t.name ASC")
    List<Technology> findAllByProfileIdOrderByCategoryAndName(Long profileId);
}


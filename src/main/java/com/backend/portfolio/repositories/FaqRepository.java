package com.backend.portfolio.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portfolio.models.entities.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {

    List<Faq> findAllByProfileIdOrderByCreatedAtDesc(Long profileId);

    List<Faq> findAllByOrderByCreatedAtDesc();
}


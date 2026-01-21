package com.backend.portfolio.repositories;

import com.backend.portfolio.models.entities.Inquire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquireRepository extends JpaRepository<Inquire, Long> {

    List<Inquire> findAllByProfileIdOrderByCreatedAtDesc(Long profileId);

    List<Inquire> findAllByOrderByCreatedAtDesc();
}


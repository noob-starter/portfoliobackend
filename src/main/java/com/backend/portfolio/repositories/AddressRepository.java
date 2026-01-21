package com.backend.portfolio.repositories;

import com.backend.portfolio.models.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByProfileIdOrderByCreatedAtDesc(Long profileId);

    List<Address> findAllByOrderByCreatedAtDesc();
}


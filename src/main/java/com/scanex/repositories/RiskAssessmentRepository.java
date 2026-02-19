package com.scanex.repositories;

import com.scanex.domain.entities.Address;
import com.scanex.domain.entities.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, UUID> {

    Optional<RiskAssessment> findTopByAddressOrderByCreatedAtDesc(Address address);

    List<RiskAssessment> findByAddress(Address address);
}

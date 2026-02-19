package com.scanex.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "risk_assessments",
        indexes = {
                @Index(name = "idx_risk_created", columnList = "createdAt")
        }
)
public class RiskAssessment {

    @Getter
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false, length = 10)
    private String level;

    @Column(columnDefinition = "jsonb")
    private String reasons;

    @Column(nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    protected RiskAssessment() {}

    public RiskAssessment(int score, String level, String reasons, Address address) {
        this.score = score;
        this.level = level;
        this.reasons = reasons;
        this.address = address;
        this.createdAt = Instant.now();
    }

}


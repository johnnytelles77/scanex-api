package com.scanex.domain.entities;

import com.scanex.domain.enums.Chain;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(
        name = "addresses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"chain", "address"})
        },
        indexes = {
                @Index(name = "idx_chain_address", columnList = "chain,address")
        }
)
public class Address {

    // Getters (sin setters por ahora)
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Chain chain;

    @Column(nullable = false, length = 120)
    private String address;

    @Column(nullable = false)
    private Instant firstSeen;

    @Column(nullable = false)
    private Instant lastSeen;

    protected Address() {
        // JPA only
    }

    public Address(Chain chain, String address) {
        this.chain = chain;
        this.address = address;
        this.firstSeen = Instant.now();
        this.lastSeen = Instant.now();
    }

    public void touch() {
        this.lastSeen = Instant.now();
    }
}

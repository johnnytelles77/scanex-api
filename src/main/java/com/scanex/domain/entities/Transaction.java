package com.scanex.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(
        name = "transactions",
        indexes = {
                @Index(name = "idx_tx_hash", columnList = "hash"),
                @Index(name = "idx_tx_timestamp", columnList = "timestamp")
        }
)
public class Transaction {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 120, unique = true)
    private String hash;

    @Column(nullable = false, length = 120)
    private String fromAddress;

    @Column(nullable = false, length = 120)
    private String toAddress;

    @Column(nullable = false, precision = 38, scale = 18)
    private BigDecimal value;

    @Column(nullable = false)
    private Instant timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    protected Transaction() {}

    public Transaction(String hash, String from, String to, BigDecimal value, Instant timestamp, Address address) {
        this.hash = hash;
        this.fromAddress = from;
        this.toAddress = to;
        this.value = value;
        this.timestamp = timestamp;
        this.address = address;
    }

}

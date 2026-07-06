
package com.natwest.bankprocessor.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private String type;
    private BigDecimal amount;
    private LocalDateTime timestamp; // [cite: 27]

    protected Transaction() {}

    public Transaction(Account account, String type, BigDecimal amount, LocalDateTime timestamp) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Getters
    public Long getId() { return id; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

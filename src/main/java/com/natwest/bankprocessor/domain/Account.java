
package com.natwest.bankprocessor.domain;

import com.natwest.bankprocessor.exception.InsufficientFundsException;
import com.natwest.bankprocessor.exception.InvalidAmountException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private String id; // Unique ID [cite: 24]

    @Column(nullable = false)
    private BigDecimal balance;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    private List<Transaction> ledger = new ArrayList<>(); // Transaction history [cite: 27]

    // JPA Requirement
    protected Account() {}

    public Account(String id, BigDecimal initialBalance) {
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Initial balance cannot be negative.");
        }
        this.id = id;
        this.balance = initialBalance;
    }

    public void deposit(BigDecimal amount) {
        validateAmountPositive(amount);
        this.balance = this.balance.add(amount);
        recordTransaction("DEPOSIT", amount); // [cite: 25]
    }

    public void withdraw(BigDecimal amount) {
        validateAmountPositive(amount);
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Transaction denied: Prevented overdraft."); // 
        }
        this.balance = this.balance.subtract(amount);
        recordTransaction("WITHDRAWAL", amount); // [cite: 25]
    }

    private void validateAmountPositive(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero."); // 
        }
    }

    private void recordTransaction(String type, BigDecimal amount) {
        Transaction tx = new Transaction(this, type, amount, LocalDateTime.now());
        this.ledger.add(tx);
    }

    // Getters
    public String getId() { return id; }
    public BigDecimal getBalance() { return balance; }
    public List<Transaction> getLedger() { return ledger; }
}

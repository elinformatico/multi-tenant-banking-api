package com.banking.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction Entity - Represents a financial transaction
 *
 * This entity records all deposits and withdrawals for accounts.
 * Each transaction belongs to both an account and a tenant.
 *
 * TransactionType enum defines the two types of transactions:
 * - DEPOSIT: Money added to account
 * - WITHDRAWAL: Money removed from account
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionId;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String tenantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    // Automatically set timestamp before persisting
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // Constructors
    public Transaction() {}

    public Transaction(String accountId, String tenantId, TransactionType type, BigDecimal amount) {
        this.accountId = accountId;
        this.tenantId = tenantId;
        this.type = type;
        this.amount = amount;
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
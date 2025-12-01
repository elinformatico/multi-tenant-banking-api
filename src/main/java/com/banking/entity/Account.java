package com.banking.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Account Entity - Represents a bank account in the system
 *
 * This entity stores account information and is mapped to the database.
 * Each account belongs to a specific tenant (bank/branch).
 *
 * Key fields:
 * - accountId: Unique identifier (auto-generated UUID)
 * - tenantId: Identifies which bank/branch owns this account
 * - customerName: Name of the account holder
 * - balance: Current account balance
 * - createdAt: Timestamp when account was created
 */
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Automatically set creation timestamp before persisting
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public Account() {}

    public Account(String tenantId, String customerName, BigDecimal balance) {
        this.tenantId = tenantId;
        this.customerName = customerName;
        this.balance = balance;
    }

    // Getters and Setters
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
package com.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * AccountRequest - DTO for creating/updating accounts
 *
 * Uses validation annotations to ensure data quality:
 * - @NotBlank: Field cannot be null or empty
 * - @NotNull: Field cannot be null
 * - @Positive: Number must be greater than zero
 */
public class AccountRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotNull(message = "Balance is required")
    @Positive(message = "Balance must be positive")
    private BigDecimal balance;

    // Constructors
    public AccountRequest() {}

    public AccountRequest(String customerName, BigDecimal balance) {
        this.customerName = customerName;
        this.balance = balance;
    }

    // Getters and Setters
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
}
package com.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * TransactionRequest - DTO for creating transactions
 */
public class TransactionRequest {

    @NotBlank(message = "Transaction type is required")
    private String type; // DEPOSIT or WITHDRAWAL

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    // Constructors
    public TransactionRequest() {}

    public TransactionRequest(String type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
package com.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * StatementRequest - DTO for requesting account statements
 *
 * Client provides:
 * - accountId: Which account to generate statement for
 * - startDate: Beginning of date range
 * - endDate: End of date range
 */
public class StatementRequest {

    @NotBlank(message = "Account ID is required")
    private String accountId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    // Constructors
    public StatementRequest() {}

    public StatementRequest(String accountId, LocalDate startDate, LocalDate endDate) {
        this.accountId = accountId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
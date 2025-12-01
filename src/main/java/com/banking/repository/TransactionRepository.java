package com.banking.repository;

import com.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TransactionRepository - Data access layer for Transaction entity
 *
 * Provides methods to:
 * - Find transactions by account and tenant
 * - Find transactions within a date range (for statement generation)
 *
 * All queries include tenantId to maintain data isolation between tenants
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    /**
     * Find all transactions for a specific account belonging to a tenant
     * Used for GET /api/accounts/{accountId}/transactions
     *
     * @param accountId The account ID to search for
     * @param tenantId The tenant ID (ensures data isolation)
     * @return List of transactions for the account
     */
    List<Transaction> findByAccountIdAndTenantId(String accountId, String tenantId);

    /**
     * Find transactions within a date range for statement generation
     * Returns all transactions for an account between startDate and endDate
     * This is used by the async statement generation process
     *
     * @param accountId The account ID
     * @param tenantId The tenant ID (ensures data isolation)
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return List of transactions in the date range
     */
    List<Transaction> findByAccountIdAndTenantIdAndTimestampBetween(
            String accountId,
            String tenantId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
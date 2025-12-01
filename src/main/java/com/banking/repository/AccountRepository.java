package com.banking.repository;

import com.banking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * AccountRepository - Data access layer for Account entity
 *
 * Spring Data JPA automatically implements these methods.
 * The key methods filter by tenantId to ensure data isolation.
 *
 * Multi-tenant filtering:
 * - findByTenantId: Get all accounts for a specific tenant
 * - findByAccountIdAndTenantId: Get specific account only if it belongs to tenant
 *
 * This prevents tenants from accessing each other's data.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * Find all accounts belonging to a specific tenant
     * Used for GET /api/accounts
     */
    List<Account> findByTenantId(String tenantId);

    /**
     * Find a specific account only if it belongs to the tenant
     * This ensures tenant isolation - a tenant can only access their own accounts
     */
    Optional<Account> findByAccountIdAndTenantId(String accountId, String tenantId);

    /**
     * Delete an account only if it belongs to the tenant
     */
    void deleteByAccountIdAndTenantId(String accountId, String tenantId);
}
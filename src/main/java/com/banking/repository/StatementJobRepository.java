package com.banking.repository;

import com.banking.entity.StatementJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * StatementJobRepository - Data access layer for StatementJob entity
 *
 * Manages async statement generation jobs.
 * Ensures tenants can only access their own jobs.
 */
@Repository
public interface StatementJobRepository extends JpaRepository<StatementJob, String> {

    /**
     * Find a job only if it belongs to the requesting tenant
     * This prevents tenants from seeing each other's statement jobs
     */
    Optional<StatementJob> findByJobIdAndTenantId(String jobId, String tenantId);
}
package com.banking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * StatementJob Entity - Tracks asynchronous statement generation jobs
 *
 * This entity stores information about statement generation requests:
 * - PENDING: Job created but not yet processed
 * - PROCESSING: Currently being processed
 * - COMPLETED: Successfully finished
 * - FAILED: Error occurred during processing
 *
 * The result field stores the generated statement (as JSON or text)
 */
@Entity
@Table(name = "statement_jobs")
public class StatementJob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String jobId;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(length = 10000)
    private String result;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = JobStatus.PENDING;
        }
    }

    // Constructors
    public StatementJob() {}

    public StatementJob(String accountId, String tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        this.accountId = accountId;
        this.tenantId = tenantId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = JobStatus.PENDING;
    }

    // Getters and Setters
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}

/**
 * Enum for job status tracking
 */
enum JobStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED
}
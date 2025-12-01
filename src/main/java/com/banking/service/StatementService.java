package com.banking.service;

import com.banking.config.TenantContext;
import com.banking.dto.StatementRequest;
import com.banking.entity.Account;
import com.banking.entity.JobStatus;
import com.banking.entity.StatementJob;
import com.banking.entity.Transaction;
import com.banking.entity.TransactionType;
import com.banking.repository.AccountRepository;
import com.banking.repository.StatementJobRepository;
import com.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * StatementService - Handles asynchronous statement generation
 *
 * This service demonstrates async processing:
 * 1. Client requests statement → receives jobId immediately
 * 2. Statement is generated in background thread
 * 3. Client polls for job status and result
 *
 * The @Async annotation makes methods run in a separate thread pool
 */
@Service
@Transactional
public class StatementService {

    @Autowired
    private StatementJobRepository jobRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Request a statement generation
     * Returns immediately with a job ID
     */
    public StatementJob requestStatement(StatementRequest request) {
        String tenantId = TenantContext.getTenantId();
        String accountId = request.getAccountId();

        // Verify account exists and belongs to tenant
        accountRepository.findByAccountIdAndTenantId(accountId, tenantId)
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));

        // Create job record
        LocalDateTime startDateTime = request.getStartDate().atStartOfDay();
        LocalDateTime endDateTime = request.getEndDate().atTime(23, 59, 59);

        StatementJob job = new StatementJob(
                accountId,
                tenantId,
                startDateTime,
                endDateTime
        );

        job = jobRepository.save(job);

        // Trigger async processing
        // We pass the jobId and tenantId explicitly because @Async runs in a different thread
        // and won't have access to the ThreadLocal TenantContext
        processStatementAsync(job.getJobId(), tenantId);

        return job;
    }

    /**
     * Process statement generation asynchronously
     *
     * @Async makes this run in a background thread
     * The method returns void and processes independently
     *
     * Note: We pass tenantId as parameter because this runs in a different thread
     * and doesn't have access to TenantContext ThreadLocal
     */
    @Async("taskExecutor")
    public void processStatementAsync(String jobId, String tenantId) {
        try {
            // Retrieve job
            StatementJob job = jobRepository.findById(jobId)
                    .orElseThrow(() -> new RuntimeException("Job not found"));

            // Update status to PROCESSING
            job.setStatus(JobStatus.PROCESSING);
            jobRepository.save(job);

            // Simulate processing time (remove in production or reduce significantly)
            Thread.sleep(3000);

            // Get account info
            Account account = accountRepository.findByAccountIdAndTenantId(
                    job.getAccountId(),
                    tenantId
            ).orElseThrow(() -> new RuntimeException("Account not found"));

            // Get transactions in date range
            List<Transaction> transactions = transactionRepository
                    .findByAccountIdAndTenantIdAndTimestampBetween(
                            job.getAccountId(),
                            tenantId,
                            job.getStartDate(),
                            job.getEndDate()
                    );

            // Calculate opening balance (current balance - all transactions in period)
            BigDecimal openingBalance = account.getBalance();
            for (Transaction t : transactions) {
                if (t.getType() == TransactionType.DEPOSIT) {
                    openingBalance = openingBalance.subtract(t.getAmount());
                } else {
                    openingBalance = openingBalance.add(t.getAmount());
                }
            }

            // Generate statement result
            StringBuilder statement = new StringBuilder();
            statement.append("=== ACCOUNT STATEMENT ===\n");
            statement.append("Account ID: ").append(account.getAccountId()).append("\n");
            statement.append("Customer: ").append(account.getCustomerName()).append("\n");
            statement.append("Period: ").append(job.getStartDate()).append(" to ")
                    .append(job.getEndDate()).append("\n\n");
            statement.append("Opening Balance: $").append(openingBalance).append("\n\n");
            statement.append("TRANSACTIONS:\n");

            for (Transaction t : transactions) {
                statement.append(t.getTimestamp()).append(" | ")
                        .append(t.getType()).append(" | $")
                        .append(t.getAmount()).append("\n");
            }

            statement.append("\nClosing Balance: $").append(account.getBalance()).append("\n");
            statement.append("========================");

            // Update job with result
            job.setResult(statement.toString());
            job.setStatus(JobStatus.COMPLETED);
            job.setCompletedAt(LocalDateTime.now());
            jobRepository.save(job);

        } catch (Exception e) {
            // Mark job as failed
            StatementJob job = jobRepository.findById(jobId).orElse(null);
            if (job != null) {
                job.setStatus(JobStatus.FAILED);
                job.setResult("Error: " + e.getMessage());
                job.setCompletedAt(LocalDateTime.now());
                jobRepository.save(job);
            }
        }
    }

    /**
     * Get the status and result of a statement job
     */
    public Optional<StatementJob> getJobStatus(String jobId) {
        String tenantId = TenantContext.getTenantId();
        return jobRepository.findByJobIdAndTenantId(jobId, tenantId);
    }
}
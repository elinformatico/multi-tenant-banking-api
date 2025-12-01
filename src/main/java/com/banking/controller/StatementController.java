package com.banking.controller;

import com.banking.dto.StatementRequest;
import com.banking.entity.StatementJob;
import com.banking.service.StatementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * StatementController - REST API endpoints for async statement generation
 *
 * Endpoints:
 * - POST /api/statements        - Request statement generation (returns jobId)
 * - GET  /api/statements/{jobId} - Check job status and get result
 *
 * Flow:
 * 1. Client POSTs to /api/statements → receives jobId
 * 2. Background processing starts
 * 3. Client polls GET /api/statements/{jobId} until status is COMPLETED
 */
@RestController
@RequestMapping("/api/statements")
public class StatementController {

    @Autowired
    private StatementService statementService;

    /**
     * Request a statement generation
     *
     * POST /api/statements
     * Headers: X-Tenant-Id: BANK001
     * Body: {
     *   "accountId": "A123",
     *   "startDate": "2025-10-01",
     *   "endDate": "2025-10-31"
     * }
     *
     * Returns: { "jobId": "uuid-here", "status": "PENDING" }
     */
    @PostMapping
    public ResponseEntity<?> requestStatement(@Valid @RequestBody StatementRequest request) {
        try {
            StatementJob job = statementService.requestStatement(request);

            // Return jobId immediately so client can poll for results
            Map<String, Object> response = new HashMap<>();
            response.put("jobId", job.getJobId());
            response.put("status", job.getStatus());
            response.put("message", "Statement generation started. Poll /api/statements/" +
                    job.getJobId() + " for results");

            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get the status and result of a statement job
     *
     * GET /api/statements/{jobId}
     * Headers: X-Tenant-Id: BANK001
     *
     * Returns:
     * - If PENDING/PROCESSING: { "jobId": "...", "status": "PROCESSING" }
     * - If COMPLETED: { "jobId": "...", "status": "COMPLETED", "result": "..." }
     * - If FAILED: { "jobId": "...", "status": "FAILED", "result": "error message" }
     */
    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJobStatus(@PathVariable String jobId) {
        return statementService.getJobStatus(jobId)
                .map(job -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("jobId", job.getJobId());
                    response.put("status", job.getStatus());
                    response.put("accountId", job.getAccountId());
                    response.put("createdAt", job.getCreatedAt());
                    response.put("completedAt", job.getCompletedAt());

                    // Include result if job is completed or failed
                    if (job.getStatus().toString().equals("COMPLETED") ||
                            job.getStatus().toString().equals("FAILED")) {
                        response.put("result", job.getResult());
                    }

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
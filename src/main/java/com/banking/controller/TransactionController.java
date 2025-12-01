package com.banking.controller;

import com.banking.dto.TransactionRequest;
import com.banking.entity.Transaction;
import com.banking.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TransactionController - REST API endpoints for transaction management
 *
 * This controller handles all transaction-related operations:
 * - Creating deposits and withdrawals
 * - Retrieving transaction history for accounts
 *
 * All operations are automatically filtered by tenant via TenantFilter
 *
 * Endpoints:
 * - POST /api/accounts/{accountId}/transactions - Create a new transaction
 * - GET  /api/accounts/{accountId}/transactions - List all transactions for an account
 */
@RestController
@RequestMapping("/api/accounts")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Create a new transaction (deposit or withdrawal)
     *
     * Endpoint: POST /api/accounts/{accountId}/transactions
     *
     * Headers:
     *   X-Tenant-Id: BANK001 (required)
     *
     * Request Body:
     * {
     *   "type": "DEPOSIT",     // or "WITHDRAWAL"
     *   "amount": 500.00
     * }
     *
     * Response: 201 Created
     * {
     *   "transactionId": "uuid",
     *   "accountId": "account-uuid",
     *   "tenantId": "BANK001",
     *   "type": "DEPOSIT",
     *   "amount": 500.00,
     *   "timestamp": "2025-12-01T10:30:00"
     * }
     *
     * This endpoint also updates the account balance atomically.
     * For DEPOSIT: balance increases
     * For WITHDRAWAL: balance decreases (if sufficient funds)
     *
     * @param accountId The account ID from the URL path
     * @param request The transaction details (type and amount)
     * @return ResponseEntity with created transaction or error message
     */
    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<?> createTransaction(
            @PathVariable String accountId,
            @Valid @RequestBody TransactionRequest request) {
        try {
            Transaction transaction = transactionService.createTransaction(accountId, request);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Return error message in JSON format
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Get all transactions for a specific account
     *
     * Endpoint: GET /api/accounts/{accountId}/transactions
     *
     * Headers:
     *   X-Tenant-Id: BANK001 (required)
     *
     * Response: 200 OK
     * [
     *   {
     *     "transactionId": "uuid1",
     *     "accountId": "account-uuid",
     *     "tenantId": "BANK001",
     *     "type": "DEPOSIT",
     *     "amount": 500.00,
     *     "timestamp": "2025-12-01T10:30:00"
     *   },
     *   {
     *     "transactionId": "uuid2",
     *     "accountId": "account-uuid",
     *     "tenantId": "BANK001",
     *     "type": "WITHDRAWAL",
     *     "amount": 200.00,
     *     "timestamp": "2025-12-01T11:00:00"
     *   }
     * ]
     *
     * Only returns transactions that belong to:
     * 1. The specified account
     * 2. The current tenant (from X-Tenant-Id header)
     *
     * @param accountId The account ID from the URL path
     * @return ResponseEntity with list of transactions or error message
     */
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable String accountId) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByAccount(accountId);
            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            // Return error message in JSON format
            return ResponseEntity.badRequest()
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
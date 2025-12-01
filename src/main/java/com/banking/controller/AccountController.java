package com.banking.controller;

import com.banking.dto.AccountRequest;
import com.banking.entity.Account;
import com.banking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AccountController - REST API endpoints for account management
 *
 * Endpoints:
 * - POST   /api/accounts             - Create new account
 * - GET    /api/accounts             - List all accounts (for current tenant)
 * - GET    /api/accounts/{id}        - Get specific account
 * - PUT    /api/accounts/{id}        - Update account
 * - DELETE /api/accounts/{id}        - Delete account
 *
 * All operations automatically filtered by tenant (via TenantFilter)
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * Create a new account
     *
     * POST /api/accounts
     * Headers: X-Tenant-Id: BANK001
     * Body: { "customerName": "Alice", "balance": 1000.00 }
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountRequest request) {
        Account account = accountService.createAccount(request);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    /**
     * Get all accounts for the current tenant
     *
     * GET /api/accounts
     * Headers: X-Tenant-Id: BANK001
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    /**
     * Get a specific account by ID
     *
     * GET /api/accounts/{accountId}
     * Headers: X-Tenant-Id: BANK001
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable String accountId) {
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update an existing account
     *
     * PUT /api/accounts/{accountId}
     * Headers: X-Tenant-Id: BANK001
     * Body: { "customerName": "Alice Smith", "balance": 1500.00 }
     */
    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable String accountId,
            @Valid @RequestBody AccountRequest request) {
        try {
            Account account = accountService.updateAccount(accountId, request);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete an account
     *
     * DELETE /api/accounts/{accountId}
     * Headers: X-Tenant-Id: BANK001
     */
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        try {
            accountService.deleteAccount(accountId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

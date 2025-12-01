package com.banking.service;

import com.banking.config.TenantContext;
import com.banking.dto.AccountRequest;
import com.banking.entity.Account;
import com.banking.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * AccountService - Business logic for account operations
 *
 * This service layer:
 * - Automatically applies tenant filtering using TenantContext
 * - Implements CRUD operations for accounts
 * - Ensures all operations respect multi-tenant boundaries
 *
 * @Transactional ensures database consistency
 */
@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Create a new account for the current tenant
     */
    public Account createAccount(AccountRequest request) {
        String tenantId = TenantContext.getTenantId();

        Account account = new Account(
                tenantId,
                request.getCustomerName(),
                request.getBalance()
        );

        return accountRepository.save(account);
    }

    /**
     * Get all accounts for the current tenant
     */
    public List<Account> getAllAccounts() {
        String tenantId = TenantContext.getTenantId();
        return accountRepository.findByTenantId(tenantId);
    }

    /**
     * Get a specific account (only if it belongs to current tenant)
     */
    public Optional<Account> getAccountById(String accountId) {
        String tenantId = TenantContext.getTenantId();
        return accountRepository.findByAccountIdAndTenantId(accountId, tenantId);
    }

    /**
     * Update an existing account
     * Only updates if account belongs to current tenant
     */
    public Account updateAccount(String accountId, AccountRequest request) {
        String tenantId = TenantContext.getTenantId();

        Account account = accountRepository.findByAccountIdAndTenantId(accountId, tenantId)
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));

        account.setCustomerName(request.getCustomerName());
        account.setBalance(request.getBalance());

        return accountRepository.save(account);
    }

    /**
     * Delete an account (only if it belongs to current tenant)
     */
    public void deleteAccount(String accountId) {
        String tenantId = TenantContext.getTenantId();

        // Verify account exists and belongs to tenant
        accountRepository.findByAccountIdAndTenantId(accountId, tenantId)
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));

        accountRepository.deleteByAccountIdAndTenantId(accountId, tenantId);
    }
}
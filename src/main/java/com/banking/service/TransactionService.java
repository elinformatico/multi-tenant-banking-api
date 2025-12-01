package com.banking.service;

import com.banking.config.TenantContext;
import com.banking.dto.TransactionRequest;
import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.entity.TransactionType;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * TransactionService - Business logic for transaction operations
 *
 * This service:
 * - Creates deposits and withdrawals
 * - Updates account balances atomically
 * - Validates transaction rules (e.g., sufficient balance for withdrawals)
 * - Maintains tenant isolation
 */
@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Create a new transaction (DEPOSIT or WITHDRAWAL)
     * Also updates the account balance atomically
     */
    public Transaction createTransaction(String accountId, TransactionRequest request) {
        String tenantId = TenantContext.getTenantId();

        // Verify account exists and belongs to tenant
        Account account = accountRepository.findByAccountIdAndTenantId(accountId, tenantId)
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));

        // Validate and apply transaction
        BigDecimal amount = request.getAmount();
        String typeStr = request.getType().toUpperCase();

        TransactionType transactionType;

        if (typeStr.equals("DEPOSIT")) {
            transactionType = TransactionType.DEPOSIT;
            // Add money to account
            account.setBalance(account.getBalance().add(amount));
        } else if (typeStr.equals("WITHDRAWAL")) {
            transactionType = TransactionType.WITHDRAWAL;
            // Check sufficient balance
            if (account.getBalance().compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance");
            }
            // Subtract money from account
            account.setBalance(account.getBalance().subtract(amount));
        } else {
            throw new RuntimeException("Invalid transaction type. Use DEPOSIT or WITHDRAWAL");
        }

        // Save updated account balance
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = new Transaction(
                accountId,
                tenantId,
                transactionType,
                amount
        );

        return transactionRepository.save(transaction);
    }

    /**
     * Get all transactions for a specific account (tenant-filtered)
     */
    public List<Transaction> getTransactionsByAccount(String accountId) {
        String tenantId = TenantContext.getTenantId();

        // Verify account exists and belongs to tenant
        accountRepository.findByAccountIdAndTenantId(accountId, tenantId)
                .orElseThrow(() -> new RuntimeException("Account not found or access denied"));

        return transactionRepository.findByAccountIdAndTenantId(accountId, tenantId);
    }
}
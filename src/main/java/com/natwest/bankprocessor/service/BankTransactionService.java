
package com.natwest.bankprocessor.service;

import com.natwest.bankprocessor.domain.Account;
import com.natwest.bankprocessor.exception.AccountNotFoundException;
import com.natwest.bankprocessor.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class BankTransactionService {

    private final AccountRepository accountRepository;

    public BankTransactionService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void deposit(String accountId, BigDecimal amount) {
        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        account.deposit(amount);
        accountRepository.save(account);
    }

    @Transactional
    public void withdraw(String accountId, BigDecimal amount) {
        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        account.withdraw(amount);
        accountRepository.save(account);
    }

    @Transactional
    public void transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        // Edge Case Prevention: Establish a deterministic locking order to eliminate deadlocks 
        String firstLock = fromAccountId.compareTo(toAccountId) < 0 ? fromAccountId : toAccountId;
        String secondLock = firstLock.equals(fromAccountId) ? toAccountId : fromAccountId;

        Account first = accountRepository.findByIdForUpdate(firstLock)
                .orElseThrow(() -> new AccountNotFoundException(firstLock));
        Account second = accountRepository.findByIdForUpdate(secondLock)
                .orElseThrow(() -> new AccountNotFoundException(secondLock));

        Account fromAccount = first.getId().equals(fromAccountId) ? first : second;
        Account toAccount = first.getId().equals(toAccountId) ? first : second;

        fromAccount.withdraw(amount); // [cite: 25]
        toAccount.deposit(amount);    // [cite: 25]

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}

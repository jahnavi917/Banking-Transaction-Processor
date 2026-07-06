
package com.natwest.bankprocessor.web;

import com.natwest.bankprocessor.domain.Account;
import com.natwest.bankprocessor.repository.AccountRepository;
import com.natwest.bankprocessor.service.BankTransactionService;
import com.natwest.bankprocessor.web.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/accounts")
public class BankController {

    private final BankTransactionService transactionService;
    private final AccountRepository accountRepository;

    public BankController(BankTransactionService transactionService, AccountRepository accountRepository) {
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/{id}/balance") // 
    public ResponseEntity<AccountResponse> getBalance(@PathVariable String id) {
        return accountRepository.findById(id)
                .map(acc -> ResponseEntity.ok(new AccountResponse(acc.getId(), acc.getBalance())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/history") // 
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@PathVariable String id) {
        return accountRepository.findById(id)
                .map(acc -> {
                    List<TransactionResponse> history = acc.getLedger().stream()
                            .map(tx -> new TransactionResponse(tx.getType(), tx.getAmount(), tx.getTimestamp()))
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(history);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/deposit") // [cite: 25]
    public ResponseEntity<Void> deposit(@PathVariable String id, @RequestBody TransactionRequest request) {
        transactionService.deposit(id, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/withdraw") // [cite: 25]
    public ResponseEntity<Void> withdraw(@PathVariable String id, @RequestBody TransactionRequest request) {
        transactionService.withdraw(id, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer") // [cite: 25]
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
        transactionService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}

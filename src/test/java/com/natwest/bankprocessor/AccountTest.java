
package com.natwest.bankprocessor;

import com.natwest.bankprocessor.domain.Account;
import com.natwest.bankprocessor.exception.InsufficientFundsException;
import com.natwest.bankprocessor.exception.InvalidAmountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    @DisplayName("Should incrementally accrue balance upon valid deposit operations")
    void verifyDeposits() {
        Account account = new Account("ACC-001", new BigDecimal("100.00"));
        account.deposit(new BigDecimal("50.25"));
        assertEquals(0, new BigDecimal("150.25").compareTo(account.getBalance()));
        assertEquals(1, account.getLedger().size());
    }

    @Test
    @DisplayName("Should strictly reject withdrawals that result in an overdraft state")
    void verifyOverdraftRejection() {
        Account account = new Account("ACC-002", new BigDecimal("10.00"));
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(new BigDecimal("10.01")));
        assertEquals(0, new BigDecimal("10.00").compareTo(account.getBalance()));
    }

    @Test
    @DisplayName("Should reject processing explicit negative amounts")
    void verifyNegativeBounds() {
        Account account = new Account("ACC-003", new BigDecimal("50.00"));
        assertThrows(InvalidAmountException.class, () -> account.deposit(new BigDecimal("-5.00")));
    }
}

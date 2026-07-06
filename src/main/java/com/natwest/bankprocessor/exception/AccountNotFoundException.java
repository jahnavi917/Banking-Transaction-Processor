// AccountNotFoundException.java
package com.natwest.bankprocessor.exception;
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String id) { super("Account with ID " + id + " was not located."); }
}

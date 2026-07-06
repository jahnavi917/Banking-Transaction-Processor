// InsufficientFundsException.java
package com.natwest.bankprocessor.exception;
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String msg) { super(msg); }
}

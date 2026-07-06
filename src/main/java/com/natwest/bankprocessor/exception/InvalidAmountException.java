// InvalidAmountException.java
package com.natwest.bankprocessor.exception;
public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String msg) { super(msg); }
}

// TransactionRequest.java
package com.natwest.bankprocessor.web.dto;
import java.math.BigDecimal;
public class TransactionRequest {
    private BigDecimal amount;
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}

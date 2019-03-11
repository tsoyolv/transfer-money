package com.tsoyolv.transfermoney.model;

import com.tsoyolv.transfermoney.model.annotation.Id;

import java.math.BigDecimal;

public class Account implements DbEntity {
    @Id
    private Long accountId;

    private String accountNumber;

    private BigDecimal balance;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

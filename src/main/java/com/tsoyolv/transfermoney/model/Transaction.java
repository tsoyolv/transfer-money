package com.tsoyolv.transfermoney.model;

import com.tsoyolv.transfermoney.model.annotation.Id;

import java.math.BigDecimal;

public class Transaction implements DbEntity {

    @Id
    private long transactionId;

    private long accountFromId;

    private long accountToId;

    private BigDecimal amount;

    private String currencyCode;

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(long accountFromId) {
        this.accountFromId = accountFromId;
    }

    public long getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(long accountToId) {
        this.accountToId = accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "accountFrom=" + accountFromId +
                ", accountTo=" + accountToId +
                ", amount=" + amount +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }
}

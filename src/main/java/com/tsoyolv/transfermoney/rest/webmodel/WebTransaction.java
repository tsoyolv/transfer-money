package com.tsoyolv.transfermoney.rest.webmodel;

import java.math.BigDecimal;

public class WebTransaction {

    public WebTransaction() {
    }

    public WebTransaction(long accountFrom, long accountTo, BigDecimal amount, String currencyCode) {
        this.accountFromId = accountFrom;
        this.accountToId = accountTo;
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    private long accountFromId;

    private long accountToId;

    private BigDecimal amount;

    private String currencyCode;

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
        return "WebTransaction{" +
                "accountFrom=" + accountFromId +
                ", accountTo=" + accountToId +
                ", amount=" + amount +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }
}

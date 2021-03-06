/*
 * This file is generated by jOOQ.
 */
package com.tsoyolv.transfermoney.generated.jooq;


import com.tsoyolv.transfermoney.generated.jooq.tables.Account;
import com.tsoyolv.transfermoney.generated.jooq.tables.Transaction;
import com.tsoyolv.transfermoney.generated.jooq.tables.User;

import javax.annotation.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables of the <code>PUBLIC</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index PRIMARY_KEY_E = Indexes0.PRIMARY_KEY_E;
    public static final Index UX_ACCOUNT_USERNAME_CURRENCY = Indexes0.UX_ACCOUNT_USERNAME_CURRENCY;
    public static final Index UX_TRANSACTION_TO_ACCOUNT_FROMID = Indexes0.UX_TRANSACTION_TO_ACCOUNT_FROMID;
    public static final Index UX_TRANSACTION_TO_ACCOUNT_TOID = Indexes0.UX_TRANSACTION_TO_ACCOUNT_TOID;
    public static final Index PRIMARY_KEY_F = Indexes0.PRIMARY_KEY_F;
    public static final Index PRIMARY_KEY_2 = Indexes0.PRIMARY_KEY_2;
    public static final Index UX_USER_EMAIL_USERNAME = Indexes0.UX_USER_EMAIL_USERNAME;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index PRIMARY_KEY_E = Internal.createIndex("PRIMARY_KEY_E", Account.ACCOUNT, new OrderField[] { Account.ACCOUNT.ACCOUNTID }, true);
        public static Index UX_ACCOUNT_USERNAME_CURRENCY = Internal.createIndex("UX_ACCOUNT_USERNAME_CURRENCY", Account.ACCOUNT, new OrderField[] { Account.ACCOUNT.USERNAME, Account.ACCOUNT.CURRENCYCODE }, true);
        public static Index UX_TRANSACTION_TO_ACCOUNT_FROMID = Internal.createIndex("UX_TRANSACTION_TO_ACCOUNT_FROMID", Account.ACCOUNT, new OrderField[] { Account.ACCOUNT.ACCOUNTID }, false);
        public static Index UX_TRANSACTION_TO_ACCOUNT_TOID = Internal.createIndex("UX_TRANSACTION_TO_ACCOUNT_TOID", Account.ACCOUNT, new OrderField[] { Account.ACCOUNT.ACCOUNTID }, false);
        public static Index PRIMARY_KEY_F = Internal.createIndex("PRIMARY_KEY_F", Transaction.TRANSACTION, new OrderField[] { Transaction.TRANSACTION.TRANSACTIONID }, true);
        public static Index PRIMARY_KEY_2 = Internal.createIndex("PRIMARY_KEY_2", User.USER, new OrderField[] { User.USER.USERID }, true);
        public static Index UX_USER_EMAIL_USERNAME = Internal.createIndex("UX_USER_EMAIL_USERNAME", User.USER, new OrderField[] { User.USER.USERNAME, User.USER.EMAIL }, true);
    }
}

/*
 * This file is generated by jOOQ.
 */
package com.tsoyolv.transfermoney.generated.jooq.tables;


import com.tsoyolv.transfermoney.generated.jooq.Indexes;
import com.tsoyolv.transfermoney.generated.jooq.Keys;
import com.tsoyolv.transfermoney.generated.jooq.Public;
import com.tsoyolv.transfermoney.generated.jooq.tables.records.TransactionRecord;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Transaction extends TableImpl<TransactionRecord> {

    private static final long serialVersionUID = -431891681;

    /**
     * The reference instance of <code>PUBLIC.TRANSACTION</code>
     */
    public static final Transaction TRANSACTION = new Transaction();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TransactionRecord> getRecordType() {
        return TransactionRecord.class;
    }

    /**
     * The column <code>PUBLIC.TRANSACTION.TRANSACTIONID</code>.
     */
    public final TableField<TransactionRecord, BigInteger> TRANSACTIONID = createField("TRANSACTIONID", org.jooq.impl.SQLDataType.DECIMAL_INTEGER.precision(38).nullable(false).identity(true), this, "");

    /**
     * The column <code>PUBLIC.TRANSACTION.ACCOUNTFROMID</code>.
     */
    public final TableField<TransactionRecord, BigInteger> ACCOUNTFROMID = createField("ACCOUNTFROMID", org.jooq.impl.SQLDataType.DECIMAL_INTEGER.precision(38).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.TRANSACTION.ACCOUNTTOID</code>.
     */
    public final TableField<TransactionRecord, BigInteger> ACCOUNTTOID = createField("ACCOUNTTOID", org.jooq.impl.SQLDataType.DECIMAL_INTEGER.precision(38).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.TRANSACTION.AMOUNT</code>.
     */
    public final TableField<TransactionRecord, BigDecimal> AMOUNT = createField("AMOUNT", org.jooq.impl.SQLDataType.DECIMAL(18, 2), this, "");

    /**
     * The column <code>PUBLIC.TRANSACTION.CURRENCYCODE</code>.
     */
    public final TableField<TransactionRecord, String> CURRENCYCODE = createField("CURRENCYCODE", org.jooq.impl.SQLDataType.VARCHAR(30), this, "");

    /**
     * Create a <code>PUBLIC.TRANSACTION</code> table reference
     */
    public Transaction() {
        this(DSL.name("TRANSACTION"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.TRANSACTION</code> table reference
     */
    public Transaction(String alias) {
        this(DSL.name(alias), TRANSACTION);
    }

    /**
     * Create an aliased <code>PUBLIC.TRANSACTION</code> table reference
     */
    public Transaction(Name alias) {
        this(alias, TRANSACTION);
    }

    private Transaction(Name alias, Table<TransactionRecord> aliased) {
        this(alias, aliased, null);
    }

    private Transaction(Name alias, Table<TransactionRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PRIMARY_KEY_F);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<TransactionRecord, BigInteger> getIdentity() {
        return Keys.IDENTITY_TRANSACTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<TransactionRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_F;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<TransactionRecord>> getKeys() {
        return Arrays.<UniqueKey<TransactionRecord>>asList(Keys.CONSTRAINT_F);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction as(String alias) {
        return new Transaction(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction as(Name alias) {
        return new Transaction(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Transaction rename(String name) {
        return new Transaction(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Transaction rename(Name name) {
        return new Transaction(name, null);
    }
}
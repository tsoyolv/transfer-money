package com.tsoyolv.transfermoney.guice.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tsoyolv.transfermoney.LoggerWrapper;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConnectionProvider;

import javax.sql.DataSource;

import java.sql.SQLException;

import static com.tsoyolv.transfermoney.LoggerWrapper.JDBC_LOGGER_NAME;

public class DslContextProvider implements Provider<DSLContext> {

    private static final LoggerWrapper log = LoggerWrapper.getLogger(JDBC_LOGGER_NAME);

    private DataSource dataSource;

    private SQLDialect sqlDialect;

    @Inject(optional = true)
    private Settings jooqSettings = null;

    @Inject(optional = true)
    private Configuration configuration = null;

    @Inject
    public DslContextProvider(final DataSource jdbcSource, final SQLDialect sqlDialect) {
        this.dataSource = jdbcSource;
        this.sqlDialect = sqlDialect;
    }

    @Override
    public DSLContext get() {
        DefaultConnectionProvider conn;
        try {
            log.debug("Getting JDBC connection");
            conn = new DefaultConnectionProvider(dataSource.getConnection());
        } catch (SQLException e) {
            log.error("Connection provider creation failed!", e);
            throw new RuntimeException(e);
        }

        if (configuration != null) {
            log.debug("Creating factory from configuration having dialect {}", configuration.dialect());
            if (jooqSettings != null) {
                log.warn("@Injected org.jooq.conf.Settings is being ignored since a full org.jooq.Configuration was supplied");
            }
            return DSL.using(configuration);
        } else {
            if (jooqSettings == null) {
                log.debug("Creating factory with dialect {}", sqlDialect);
                return DSL.using(conn, sqlDialect);
            } else {
                log.debug("Creating factory with dialect {} and settings.", sqlDialect);
                return DSL.using(conn, sqlDialect, jooqSettings);
            }
        }
    }
}

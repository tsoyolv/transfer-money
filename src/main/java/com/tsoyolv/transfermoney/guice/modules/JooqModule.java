package com.tsoyolv.transfermoney.guice.modules;

import com.google.inject.AbstractModule;
import com.tsoyolv.transfermoney.ApplicationProperties;
import com.tsoyolv.transfermoney.guice.providers.DslContextProvider;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;

import javax.sql.DataSource;

import static com.tsoyolv.transfermoney.ApplicationProperties.DB_CONNECTION_URL_PROPERTY_NAME;
import static com.tsoyolv.transfermoney.ApplicationProperties.DB_PASSWORD_PROPERTY_NAME;
import static com.tsoyolv.transfermoney.ApplicationProperties.DB_USER_PROPERTY_NAME;

public class JooqModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SQLDialect.class).toInstance(SQLDialect.H2);
        bind(DataSource.class).toInstance(createDataSource());
        bind(DSLContext.class).toProvider(DslContextProvider.class);
    }

    private DataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(ApplicationProperties.getProperty(DB_CONNECTION_URL_PROPERTY_NAME));
        dataSource.setUser(ApplicationProperties.getProperty(DB_USER_PROPERTY_NAME));
        dataSource.setPassword(ApplicationProperties.getProperty(DB_PASSWORD_PROPERTY_NAME));
        return dataSource;
    }
}

package com.tsoyolv.transfermoney.guice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.tsoyolv.transfermoney.embeddedserver.EmbeddedServer;
import com.tsoyolv.transfermoney.embeddedserver.impl.spark.SparkEmbeddedServer;
import com.tsoyolv.transfermoney.logic.dao.AccountDao;
import com.tsoyolv.transfermoney.logic.dao.impl.jdbc.JdbcAccountDao;
import com.tsoyolv.transfermoney.logic.dao.impl.jooq.JooqAccountDao;
import com.tsoyolv.transfermoney.logic.service.AccountService;
import com.tsoyolv.transfermoney.logic.service.impl.AccountServiceImpl;
import com.tsoyolv.transfermoney.rest.controller.spark.AccountController;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new JooqModule());
        bind(EmbeddedServer.class).to(SparkEmbeddedServer.class);
        //bind(EmbeddedServer.class).annotatedWith(Names.named("jettyEmbeddedServer")).to(JettyEmbeddedServer.class);

        // controllers
        bind(AccountController.class);

        // services
        bind(MapperFacade.class).toInstance(new DefaultMapperFactory.Builder().build().getMapperFacade());
        bind(AccountService.class).to(AccountServiceImpl.class);

        // daos
        bind(AccountDao.class).annotatedWith(Names.named("jooqAccountDao")).to(JooqAccountDao.class).in(Scopes.SINGLETON);
        bind(AccountDao.class).annotatedWith(Names.named("jdbcAccountDao")).to(JdbcAccountDao.class).in(Scopes.SINGLETON);
    }
}

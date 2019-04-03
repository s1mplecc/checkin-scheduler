package com.caacetc.scheduling.plan.configurations;

import org.jooq.ExecuteListener;
import org.jooq.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class JooqConfiguration {

    @Resource
    private DataSource dataSource;

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider
                (new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }

    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration
                .set(new DefaultExecuteListenerProvider(exceptionTransformer()));

        return jooqConfiguration;
    }

    private ExecuteListener exceptionTransformer() {
        return new DefaultExecuteListener();
    }
}

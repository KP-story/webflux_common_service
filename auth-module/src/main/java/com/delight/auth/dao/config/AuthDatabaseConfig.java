package com.delight.auth.dao.config;

import com.delight.gaia.jpa.config.DatasourceConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories(entityOperationsRef = "authEntityTemplate", basePackages = "com.delight.auth.dao")
public class AuthDatabaseConfig extends DatasourceConfiguration {


    public AuthDatabaseConfig(ConfigurableEnvironment environment) throws Exception {
        super(environment);
    }

    @Bean("authConnectionFactory")
    public ConnectionFactory connectionFactory() {
        return super.connectionFactory();
    }

    @Bean
    public ReactiveTransactionManager authTransactionManager(@Qualifier("authConnectionFactory") ConnectionFactory connectionFactory) {
        return super.transactionManager(connectionFactory);
    }


    @Bean
    public R2dbcEntityTemplate authEntityTemplate(@Qualifier("authConnectionFactory") ConnectionFactory connectionFactory) {
        return super.r2dbcEntityTemplate(connectionFactory);
    }


    @Override
    protected String getPrefixConfig() {
        return "auth-datasource";
    }
}

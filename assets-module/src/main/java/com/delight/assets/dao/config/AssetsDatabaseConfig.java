package com.delight.assets.dao.config;

import com.delight.gaia.jpa.config.DatasourceConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories(entityOperationsRef = "assetsEntityTemplate", basePackages = "com.delight.assets.dao")
public class AssetsDatabaseConfig extends DatasourceConfiguration {


    public AssetsDatabaseConfig(ConfigurableEnvironment environment) throws Exception {
        super(environment);
    }

    @Bean("assetsConnectionFactory")
    public ConnectionFactory connectionFactory() {
        return super.connectionFactory();
    }

    @Bean
    public ReactiveTransactionManager assetsTransactionManager(@Qualifier("assetsConnectionFactory") ConnectionFactory connectionFactory) {
        return super.transactionManager(connectionFactory);
    }

    @Bean
    public R2dbcEntityOperations assetsEntityTemplate(@Qualifier("assetsConnectionFactory") ConnectionFactory connectionFactory) {
        return super.r2dbcEntityTemplate(connectionFactory);
    }


    @Override
    protected String getPrefixConfig() {
        return "assets-datasource";
    }
}

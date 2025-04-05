package org.burgas.databaseserver.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class DatabaseConfig {

    @Bean
    public CompositeDatabasePopulator compositeDatabasePopulator() {
        CompositeDatabasePopulator compositeDatabasePopulator = new CompositeDatabasePopulator();
        compositeDatabasePopulator.setPopulators(
                new ResourceDatabasePopulator(new ClassPathResource("database/clean.sql")),
                new ResourceDatabasePopulator(new ClassPathResource("database/populate.sql"))
        );
        return compositeDatabasePopulator;
    }

    @Bean
    public ConnectionFactoryInitializer connectionFactoryInitializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer connectionFactoryInitializer = new ConnectionFactoryInitializer();
        connectionFactoryInitializer.setConnectionFactory(connectionFactory);
        connectionFactoryInitializer.setDatabasePopulator(compositeDatabasePopulator());
        return connectionFactoryInitializer;
    }
}

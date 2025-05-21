package org.burgas.flightbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.CompositeDatabasePopulator;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public CompositeDatabasePopulator compositeDatabasePopulator() {
        CompositeDatabasePopulator compositeDatabasePopulator = new CompositeDatabasePopulator();
        compositeDatabasePopulator.setPopulators(
                new ResourceDatabasePopulator(new ClassPathResource("database/clean.sql")),
                new ResourceDatabasePopulator(new ClassPathResource("database/init.sql")),
                new ResourceDatabasePopulator(new ClassPathResource("database/populate.sql"))
        );
        return compositeDatabasePopulator;
    }

    @Bean
    public DataSourceInitializer connectionFactoryInitializer(DataSource dataSource) {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(compositeDatabasePopulator());
        return dataSourceInitializer;
    }
}

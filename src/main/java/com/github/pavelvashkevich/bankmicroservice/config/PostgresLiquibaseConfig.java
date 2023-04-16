package com.github.pavelvashkevich.bankmicroservice.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class PostgresLiquibaseConfig {
    private final Environment environment;
    private final DataSource postgresDataSource;

   public PostgresLiquibaseConfig(Environment environment, DataSource postgresDataSource) {
        this.environment = environment;
        this.postgresDataSource = postgresDataSource;
    }

    public @Bean LiquibaseProperties postgresLiquibaseProperties() {
        LiquibaseProperties liquibaseProperties = new LiquibaseProperties();
        liquibaseProperties.setChangeLog(environment
                .getRequiredProperty("spring.config.persistence.postgres.liquibase.changelog"));
        return liquibaseProperties;
    }

    public @Bean SpringLiquibase postgresSpringLiquibase() {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(postgresDataSource);
        springLiquibase.setChangeLog(postgresLiquibaseProperties().getChangeLog());
        return springLiquibase;
    }

}

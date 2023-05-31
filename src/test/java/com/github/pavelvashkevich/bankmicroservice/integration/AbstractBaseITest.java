package com.github.pavelvashkevich.bankmicroservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractBaseITest {
    static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;

    protected final ObjectMapper objectMapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);


    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer("postgres:13")
                .withDatabaseName("bank_microservice_db_test");
        POSTGRE_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.config.persistence.postgres.driverClassName",
                POSTGRE_SQL_CONTAINER::getDriverClassName);
        registry.add("spring.config.persistence.postgres.url",
                POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.config.persistence.postgres.username",
                POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.config.persistence.postgres.password",
                POSTGRE_SQL_CONTAINER::getPassword);
    }
}

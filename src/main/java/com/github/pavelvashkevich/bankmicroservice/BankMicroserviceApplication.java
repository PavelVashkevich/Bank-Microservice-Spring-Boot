package com.github.pavelvashkevich.bankmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCassandraRepositories(basePackages = {"com.github.pavelvashkevich.bankmicroservice.repository.cassandra"})
@EnableJpaRepositories(entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager",
        basePackages = "com.github.pavelvashkevich.bankmicroservice.repository.postgres")
public class BankMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankMicroserviceApplication.class, args);
    }
}

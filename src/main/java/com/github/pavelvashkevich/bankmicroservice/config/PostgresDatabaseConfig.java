package com.github.pavelvashkevich.bankmicroservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class PostgresDatabaseConfig {
    private final Environment environment;

    @Primary
    public @Bean DataSource postgresDataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(environment.getRequiredProperty("spring.config.persistence.postgres.driverClassName"));
        driverManagerDataSource.setUrl(environment.getRequiredProperty("spring.config.persistence.postgres.url"));
        driverManagerDataSource.setUsername(environment.getRequiredProperty("spring.config.persistence.postgres.username"));
        driverManagerDataSource.setPassword(environment.getRequiredProperty("spring.config.persistence.postgres.password"));
        driverManagerDataSource.setSchema(environment.getRequiredProperty("spring.config.persistence.postgres.schema"));
        return driverManagerDataSource;
    }

    public @Bean LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(postgresDataSource());
        em.setPackagesToScan(environment.getRequiredProperty("spring.config.persistence.postgres.packageToScan"));
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());
        return em;
    }

    public @Bean PlatformTransactionManager postgresTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(postgresEntityManagerFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.jdbc.batch_size",
                environment.getRequiredProperty("spring.jpa.properties.hibernate.jdbc.batch_size"));
        properties.put("hibernate.dialect",
                environment.getRequiredProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.show_sql",
                environment.getRequiredProperty("spring.jpa.properties.hibernate.show_sql"));
        properties.put("hibernate.format_sql",
                environment.getRequiredProperty("spring.jpa.properties.hibernate.format_sql"));
        properties.put("hibernate.order_inserts",
                environment.getRequiredProperty("spring.jpa.properties.hibernate.order_inserts"));
        properties.put("hibernate.order_updates",
                environment.getRequiredProperty("spring.jpa.properties.hibernate.order_updates"));
        properties.put("hibernate.ddl-auto",
                environment.getRequiredProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.physical_naming_strategy",
                environment.getRequiredProperty("spring.jpa.hibernate.naming.physical-strategy"));
        properties.put("hibernate.implicit_naming_strategy",environment.getRequiredProperty("spring.jpa.hibernate.naming.implicit-strategy"));
        return properties;
    }
}

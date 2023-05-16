package com.github.pavelvashkevich.bankmicroservice.config;

import com.datastax.oss.driver.api.core.CqlSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.SessionFactory;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.SessionFactoryFactoryBean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;

@Configuration
@RequiredArgsConstructor
public class CassandraDatabaseConfig {
    private final Environment environment;

    public @Bean CqlSessionFactoryBean cassandraCqlSession() {
        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
        session.setContactPoints(environment.getRequiredProperty("spring.config.persistence.cassandra.contactPoints"));
        session.setLocalDatacenter(environment.getRequiredProperty("spring.config.persistence.cassandra.localDatacenter"));
        session.setKeyspaceName(environment.getRequiredProperty("spring.config.persistence.cassandra.keyspaceName"));
        return session;
    }

    public @Bean SessionFactoryFactoryBean cassandraSessionFactoryFactoryBean(CqlSession session,
                                                                              CassandraConverter cassandraConverter) {
        SessionFactoryFactoryBean sessionFactoryFactoryBean = new SessionFactoryFactoryBean();
        sessionFactoryFactoryBean.setSession(session);
        sessionFactoryFactoryBean.setConverter(cassandraConverter);
        sessionFactoryFactoryBean.setSchemaAction(SchemaAction.NONE);
        return sessionFactoryFactoryBean;
    }

    public @Bean CassandraMappingContext mappingContext(CqlSession cqlSession) {
        CassandraMappingContext mappingContext = new CassandraMappingContext();
        mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(cqlSession));
        return mappingContext;
    }

    public @Bean CassandraConverter cassandraConverter(CassandraMappingContext mappingContext) {
        return new MappingCassandraConverter(mappingContext);
    }

    public @Bean CassandraOperations cassandraTemplate(SessionFactory sessionFactory,
                                                       CassandraConverter cassandraConverter) {
        return new CassandraTemplate(sessionFactory, cassandraConverter);
    }
}

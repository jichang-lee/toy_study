package org.spring.testspring.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class QueryDslConfig {

    @PersistenceContext
    public EntityManager em;


    @Bean
    public JPAQueryFactory JpaQueryFactory(){
        return new JPAQueryFactory(em);
    }

}

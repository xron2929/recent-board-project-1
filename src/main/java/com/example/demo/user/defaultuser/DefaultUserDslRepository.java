package com.example.demo.user.defaultuser;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DefaultUserDslRepository {
    @PersistenceContext
    private EntityManager em;



}

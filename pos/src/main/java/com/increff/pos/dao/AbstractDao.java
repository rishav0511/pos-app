package com.increff.pos.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class AbstractDao {

    @PersistenceContext
    protected EntityManager em;

    protected <T> T getSingle(TypedQuery<T> query) {
        return query.getResultList().stream().findFirst().orElse(null);
    }

    protected <T> TypedQuery<T> getQuery(String jpql, Class<T> clazz) {
        return em.createQuery(jpql, clazz);
    }

    public <T> T insert(T b) {
        em.persist(b);
        return b;
    }

    public <T> T select(Class<T> c, int id) {
        return em.find(c, id);
    }

}

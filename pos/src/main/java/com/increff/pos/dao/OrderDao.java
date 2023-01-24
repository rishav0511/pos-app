package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao{
    private static String select_By_OrderId = "select p from OrderPojo p where orderId=:orderId";
    private static String select_all = "select p from OrderPojo p";
    private static String select_Between = "select p from OrderPojo p where p.createdAt between :startingDate and :endingDate";
    private static String select_After = "select p from OrderPojo p where p.createdAt >= :startingDate";
    private static String select_Before = "select p from OrderPojo p where p.createdAt <= :endingDate";

    public List<OrderPojo> selectAll() {
        TypedQuery<OrderPojo> query = em.createQuery(select_all, OrderPojo.class);
        return query.getResultList();
    }

    public OrderPojo select(int orderId) {
        TypedQuery<OrderPojo>query = em.createQuery(select_By_OrderId, OrderPojo.class);
        query.setParameter("orderId",orderId);
        return getSingle(query);
    }

    public List<OrderPojo> selectAllBetween(Date startingDate, Date endingDate) {
        TypedQuery<OrderPojo>query = em.createQuery(select_Between, OrderPojo.class);
        query.setParameter("startingDate",startingDate);
        query.setParameter("endingDate",endingDate);
        return query.getResultList();
    }

    public List<OrderPojo> selectAfter(Date startingDate) {
        TypedQuery<OrderPojo>query = em.createQuery(select_After, OrderPojo.class);
        query.setParameter("startingDate",startingDate);
        return query.getResultList();
    }

    public List<OrderPojo> selectBefore(Date endingDate) {
        TypedQuery<OrderPojo>query = em.createQuery(select_Before, OrderPojo.class);
        query.setParameter("endingDate",endingDate);
        return query.getResultList();
    }

    public void update(OrderPojo orderPojo) {
        em.merge(orderPojo);
    }


}

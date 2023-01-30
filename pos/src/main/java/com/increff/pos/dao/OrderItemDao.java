package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {
    private static String select_By_OrderId = "select p from OrderItemPojo p where orderId=:orderId";

    public List<OrderItemPojo> selectByOrderId(int orderId) {
        TypedQuery<OrderItemPojo> query = em.createQuery(select_By_OrderId, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public void deleteByOrderId(int orderId) {
        TypedQuery<OrderItemPojo> query = em.createQuery(select_By_OrderId, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        List<OrderItemPojo> list = query.getResultList();
        for (OrderItemPojo p : list) {
            em.remove(p);
        }
    }
}

package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao{
    private static String select_By_ProductId = "select p from InventoryPojo p where productId=:productId";
    private static String select_all = "select p from InventoryPojo p";

    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo>query = em.createQuery(select_all, InventoryPojo.class);
        return query.getResultList();
    }

    public InventoryPojo select(Integer productId) {
        TypedQuery<InventoryPojo>query = em.createQuery(select_By_ProductId, InventoryPojo.class);
        query.setParameter("productId",productId);
        return getSingle(query);
    }

    public void update(InventoryPojo inventoryPojo) {
        em.merge(inventoryPojo);
    }
}

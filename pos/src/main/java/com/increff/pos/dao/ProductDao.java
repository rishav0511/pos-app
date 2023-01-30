package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {
    private static String select_By_Id = "select p from ProductPojo p where id=:id";
    private static String select_By_Barcode = "select p from ProductPojo p where barcode=:barcode";
    private static String select_all = "select p from ProductPojo p";
    private static String select_Products_By_BrandId = "select p from ProductPojo p where brandId=:id";

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = em.createQuery(select_all, ProductPojo.class);
        return query.getResultList();
    }

    public ProductPojo select(String barcode) {
        TypedQuery<ProductPojo> query = em.createQuery(select_By_Barcode, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

    public ProductPojo select(Integer id) {
        TypedQuery<ProductPojo> query = em.createQuery(select_By_Id, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<ProductPojo> getProductByBrandCategory(int brandCategoryId) {
        TypedQuery<ProductPojo> query = em.createQuery(select_Products_By_BrandId, ProductPojo.class);
        query.setParameter("id", brandCategoryId);
        return query.getResultList();
    }

    public ProductPojo update(ProductPojo productPojo) {
        return em.merge(productPojo);
    }
}



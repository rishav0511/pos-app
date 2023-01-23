package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {
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

    public List<ProductPojo> getProductByBrandCategory (int brandCategoryId) {
        TypedQuery<ProductPojo> query = em.createQuery(select_Products_By_BrandId, ProductPojo.class);
        query.setParameter("id",brandCategoryId);
        return query.getResultList();
    }

    public ProductPojo update(ProductPojo productPojo) {
        return em.merge(productPojo);
    }
}



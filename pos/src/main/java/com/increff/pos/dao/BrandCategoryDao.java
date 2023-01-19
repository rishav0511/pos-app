package com.increff.pos.dao;

import com.increff.pos.pojo.BrandCategoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BrandCategoryDao extends AbstractDao{
    private static String select_By_Brand_Category = "select p from BrandCategoryPojo p where brand=:brand and category=:category";
    private static String select_id = "select p from BrandCategoryPojo p where id=:id";
    private static String select_all = "select p from BrandCategoryPojo p";
    private static String select_By_Brand = "select p from BrandCategoryPojo p where brand=:brand";
    private static String select_By_Category = "select p from BrandCategoryPojo p where category=:category";


    public BrandCategoryPojo selectByBrandCategory(String brand, String category) {
        TypedQuery<BrandCategoryPojo> query = getQuery(select_By_Brand_Category, BrandCategoryPojo.class);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        return getSingle(query);
    }

    public BrandCategoryPojo select(int id) {
        TypedQuery<BrandCategoryPojo> query = getQuery(select_id, BrandCategoryPojo.class);
        query.setParameter("id",id);
        return getSingle(query);
    }

    public List<BrandCategoryPojo> selectAll() {
        TypedQuery<BrandCategoryPojo> query = getQuery(select_all, BrandCategoryPojo.class);
        return query.getResultList();
    }

    public List<BrandCategoryPojo> selectByBrand(String brand) {
        TypedQuery<BrandCategoryPojo> query = getQuery(select_By_Brand, BrandCategoryPojo.class);
        query.setParameter("brand",brand);
        return query.getResultList();
    }

    public List<BrandCategoryPojo> selectByCategory(String category) {
        TypedQuery<BrandCategoryPojo> query = getQuery(select_By_Category, BrandCategoryPojo.class);
        query.setParameter("category",category);
        return query.getResultList();
    }

    public BrandCategoryPojo update(BrandCategoryPojo brandCategoryPojo) {
        return em.merge(brandCategoryPojo);
    }

}

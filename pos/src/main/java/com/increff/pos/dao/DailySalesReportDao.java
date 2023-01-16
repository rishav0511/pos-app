package com.increff.pos.dao;

import com.increff.pos.pojo.DailySalesReportPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class DailySalesReportDao extends AbstractDao {
    private static String select_By_Date = "select p from DailySalesReportPojo p where date=:date";
    private static String select_Between = "select p from DailySalesReportPojo p where p.date between :startingDate and :endingDate";
    private static String select_All = "select p from DailySalesReportPojo p";

    public List<DailySalesReportPojo> selectAllBetween(Date startingDate, Date endingDate) {
        TypedQuery<DailySalesReportPojo> query = em.createQuery(select_Between, DailySalesReportPojo.class);
        query.setParameter("startingDate",startingDate);
        query.setParameter("endingDate",endingDate);
        return query.getResultList();
    }

    public DailySalesReportPojo select (Date date) {
        TypedQuery<DailySalesReportPojo>query = em.createQuery(select_By_Date, DailySalesReportPojo.class);
        query.setParameter("date",date);
        return getSingle(query);
    }

    public List<DailySalesReportPojo> selectAll () {
        TypedQuery<DailySalesReportPojo>query = em.createQuery(select_All, DailySalesReportPojo.class);
        return query.getResultList();
    }
}

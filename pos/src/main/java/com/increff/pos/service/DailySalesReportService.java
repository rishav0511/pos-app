package com.increff.pos.service;

import com.increff.pos.dao.DailySalesReportDao;
import com.increff.pos.pojo.DailySalesReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class DailySalesReportService {
    @Autowired
    private DailySalesReportDao dailySalesReportDao;

    @Transactional
    public void insert(DailySalesReportPojo dailySalesReportPojo) {
        dailySalesReportDao.insert(dailySalesReportPojo);
    }

    public List<DailySalesReportPojo> select_Between(Date startingDate, Date endingDate) {
        return dailySalesReportDao.selectAllBetween(startingDate,endingDate);
    }

    public DailySalesReportPojo select_By_Date(Date date) {
        return dailySalesReportDao.select(date);
    }

    public List<DailySalesReportPojo> getAll() {
        return  dailySalesReportDao.selectAll();
    }
}

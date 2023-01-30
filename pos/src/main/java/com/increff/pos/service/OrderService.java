package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Transactional
    public OrderPojo createNewOrder() {
        OrderPojo order = new OrderPojo();
        return orderDao.insert(order);
    }

    public List<OrderPojo> getAll() {
        return orderDao.selectAll();
    }

    public OrderPojo getById(Integer id) throws ApiException {
        return getCheck(id);
    }

    public OrderPojo getCheck(Integer id) throws ApiException {
        OrderPojo order = orderDao.select(OrderPojo.class, id);
        if (order == null) {
            throw new ApiException("Order with given id not found");
        }
        return order;
    }

    @Transactional
    public void update(int id, OrderPojo orderPojo) throws ApiException {
        OrderPojo existing = getCheck(id);
        existing.setPath(orderPojo.getPath());
        orderDao.update(existing);
    }

    public String getFilePath(int orderId) throws ApiException {
        String path = getById(orderId).getPath();
        return path;
    }

    public List<OrderPojo> getAllBetween(Date startingDate, Date endingDate) throws ApiException {
        if (startingDate == null && endingDate == null) {
            return orderDao.selectAll();
        } else if (startingDate == null) {
            endingDate = TimeUtil.getEndOfDay(endingDate);
            return orderDao.selectBefore(endingDate);
        } else if (endingDate == null) {
            startingDate = TimeUtil.getStartOfDay(startingDate);
            return orderDao.selectAfter(startingDate);
        } else {
            startingDate = TimeUtil.getStartOfDay(startingDate);
            endingDate = TimeUtil.getEndOfDay(endingDate);
            if (startingDate.after(endingDate) == true) {
                throw new ApiException("Start Date can't be greater tha End Date");
            }
            return orderDao.selectAllBetween(startingDate, endingDate);
        }
    }
}

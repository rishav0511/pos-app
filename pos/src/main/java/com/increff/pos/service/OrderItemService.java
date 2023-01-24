package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.util.NormalizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;

    public List<OrderItemPojo> selectByOrderId(int orderId) {
        return orderItemDao.selectByOrderId(orderId);
    }

    @Transactional
    public void insert(OrderItemPojo orderItemPojo) {
        orderItemDao.insert(orderItemPojo);
    }

    public void insertMultiple(List<OrderItemPojo> newOrderItems) {
        for (OrderItemPojo orderItemPojo : newOrderItems) {
            orderItemDao.insert(orderItemPojo);
        }
    }

    @Transactional
    public void deleteByOrderId(int orderId) {
        orderItemDao.deleteByOrderId(orderId);
    }
}

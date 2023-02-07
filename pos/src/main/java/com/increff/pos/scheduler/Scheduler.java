package com.increff.pos.scheduler;

import com.increff.pos.pojo.DailySalesReportPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.DailySalesReportService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Scheduler {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private DailySalesReportService dailySalesReportService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void generateDailySalesReport() throws ApiException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = TimeUtil.getStartOfDay(cal.getTime());
        Date present = new Date();

        List<OrderPojo> orderPojoList = orderService.getAllBetween(yesterday, present);
        int itemsSold = 0;
        double revenue = 0;
        for (OrderPojo order : orderPojoList) {
            List<OrderItemPojo> orderItemList = orderItemService.selectByOrderId(order.getId());
            for (OrderItemPojo orderItem : orderItemList) {
                revenue = revenue + orderItem.getQuantity() * orderItem.getSellingPrice();
            }
            itemsSold += orderItemList.size();
        }
        DailySalesReportPojo dailySalesReportPojo = ConvertUtil
                .setDailySalesReportPojo(new Date(), itemsSold, orderPojoList.size(), revenue);
        dailySalesReportService.insert(dailySalesReportPojo);
    }
}

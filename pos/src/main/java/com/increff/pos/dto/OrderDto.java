package com.increff.pos.dto;

import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.GeneratePDFUtil;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemService orderItemService;

    @Transactional(rollbackFor = ApiException.class)
    public OrderData addOrder(List<OrderItemForm> orderItemForms) throws ApiException {
        ValidationUtils.validateForm(orderItemForms);
        OrderPojo orderPojo = orderService.createNewOrder();
        List<OrderItemPojo> orderItemPojos = alterInventory(orderPojo.getId(),orderItemForms);
        String path = generateInvoice(orderPojo.getId());
        orderPojo.setPath(path);
        return ConvertUtil.convertPojoToData(orderPojo,orderItemPojos);
    }

    public List<OrderItemPojo> alterInventory(int orderId,List<OrderItemForm> orderItemForms) throws ApiException {
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        for (OrderItemForm orderItemForm : orderItemForms) {
            ProductPojo productPojo = productService.checkSellingPrice(orderItemForm.getBarcode(),orderItemForm.getSellingPrice());
            OrderItemPojo orderItemPojo = ConvertUtil.
                    convertFormToPojo(orderItemForm, orderId, productPojo.getId());
            orderItemPojos.add(orderItemPojo);
            orderItemService.insert(orderItemPojo);
            inventoryService.reduce(orderItemForm.getBarcode(), orderItemPojo.getProductId(), orderItemPojo.getQuantity());
        }
        return orderItemPojos;
    }

    public List<OrderData> getAllOrders() {
        List<OrderPojo> orderPojos = orderService.getAll();
        List<OrderData> orderDatas = new ArrayList<OrderData>();
        for (OrderPojo orderPojo : orderPojos) {
            List<OrderItemPojo> orderItemPojos = orderItemService.selectByOrderId(orderPojo.getId());
            OrderData orderData = ConvertUtil.convertPojoToData(orderPojo, orderItemPojos);
            orderDatas.add(orderData);
        }
        return orderDatas;
    }

    public OrderData getOrderDetails(int id) throws ApiException {
        OrderPojo orderPojo = orderService.getById(id);
        List<OrderItemPojo> orderItemPojos = orderItemService.selectByOrderId(orderPojo.getId());
        return ConvertUtil.convertPojoToData(orderPojo, orderItemPojos);
    }

    public InvoiceData getInvoiceDataByOrderId(int orderId) throws ApiException {
        OrderPojo orderPojo = orderService.getById(orderId);
        List<OrderItemPojo> orderItemPojos = orderItemService.selectByOrderId(orderId);
        OrderData orderData = ConvertUtil.convertPojoToData(orderPojo, orderItemPojos);
        List<OrderItemData> orderItemDatas = new ArrayList<OrderItemData>();
        for (OrderItemPojo orderItemPojo : orderItemPojos) {
            ProductPojo product = productService.getProduct(orderItemPojo.getProductId());
            orderItemDatas
                    .add(ConvertUtil.convertPojoToData(orderItemPojo, product));
        }
        InvoiceData data = ConvertUtil.generateInvoice(orderData, orderItemDatas);
        return data;
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderData updateOrder(int orderId, List<OrderItemForm> orderItems) throws ApiException {
        ValidationUtils.validateForm(orderItems);
        revertInventory(orderId);
        orderItemService.deleteByOrderId(orderId);
        List<OrderItemPojo> newOrderItems = alterInventory(orderId,orderItems);
        generateInvoice(orderId);
        OrderPojo orderPojo = orderService.getById(orderId);
        return ConvertUtil.convertPojoToData(orderPojo,newOrderItems);
    }

    public String generateInvoice(Integer orderId) throws ApiException {
        try {
            InvoiceData invoiceData = getInvoiceDataByOrderId(orderId);
            return GeneratePDFUtil.generatePDF(invoiceData,orderId);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    public String getFilePath(int orderId) throws ApiException {
        return orderService.getFilePath(orderId);
    }

    public void revertInventory(int orderId) {
        List<OrderItemPojo> orderItemPojoList = orderItemService.selectByOrderId(orderId);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            inventoryService.increase(orderItemPojo.getProductId(), orderItemPojo.getQuantity());
        }
    }
}

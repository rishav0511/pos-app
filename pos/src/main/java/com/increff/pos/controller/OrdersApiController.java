package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(value = "/api/orders")
public class OrdersApiController {
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Adds a Order")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public OrderData addOrder(@RequestBody List<OrderItemForm> orderItemForms) throws ApiException {
        return orderDto.addOrder(orderItemForms);
    }

    @ApiOperation(value = "Gets a Order by orderId")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public OrderData getByOrderId(@PathVariable int id) throws ApiException {
        return orderDto.getOrderDetails(id);
    }

    @ApiOperation(value = "Gets InvoiceData by orderId")
    @RequestMapping(value = "/{id}/invoice", method = RequestMethod.GET)
    public InvoiceData getInvoiceDataByOrderId(@PathVariable int id) throws ApiException {
        return orderDto.getInvoiceDataByOrderId(id);
    }

    @ApiOperation(value = "Gets all Orders")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrderData> getAll() {
        return orderDto.getAllOrders();
    }

    @ApiOperation(value = "Update Order")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public OrderData update(@PathVariable int id, @RequestBody List<OrderItemForm> orderItemForms) throws ApiException {
        return orderDto.updateOrder(id, orderItemForms);
    }

    @ApiOperation(value = "Download invoice")
    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadInvoice(@PathVariable int id) throws ApiException, IOException {
        String fileUri = orderDto.getFilePath(id);
        File file = new File(fileUri);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    }
}

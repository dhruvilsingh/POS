package com.increff.pos.dto;

import com.increff.pos.model.data.InvoiceData;
import com.increff.pos.model.data.InvoiceItem;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.StringUtil.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceDto {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InvoiceService invoiceService;


    public ResponseEntity<Resource> getInvoice(int orderId) throws ApiException, IOException {
        orderService.getCheckCancelledStatus(orderId);
        List<OrderItemPojo> list = orderItemService.getAll(orderId);
        ZonedDateTime orderDateTime = orderService.getCheck(orderId).getTime();
        List<InvoiceItem> list2 = new ArrayList<InvoiceItem>();
        for (OrderItemPojo orderItemPojo : list) {
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            list2.add(convert(orderItemPojo, productPojo));
        }
        InvoiceData invoiceData = convert(list2, orderId, orderDateTime);
        ResponseEntity<Resource> responseEntity = invoiceService.getInvoice(invoiceData);
        orderService.update(Integer.valueOf(invoiceData.getNumber()), OrderStatus.INVOICED);
        return responseEntity;
    }

    private InvoiceItem convert(OrderItemPojo orderItemPojo, ProductPojo productPojo){
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setItemName(productPojo.getName());
        invoiceItem.setQuantity(orderItemPojo.getQuantity());
        invoiceItem.setMrp(productPojo.getMrp());
        invoiceItem.setSellingPrice(orderItemPojo.getSellingPrice());
        return invoiceItem;
    }

    private InvoiceData convert(List<InvoiceItem> list, int orderId, ZonedDateTime orderDateTime){
        InvoiceData invoiceData = new InvoiceData();
        invoiceData.setInvoiceItems(list);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = orderDateTime.format(dateTimeFormatter);
        invoiceData.setDate(formattedDateTime.toString());
        invoiceData.setNumber(String.valueOf(orderId));
        return invoiceData;
    }




}

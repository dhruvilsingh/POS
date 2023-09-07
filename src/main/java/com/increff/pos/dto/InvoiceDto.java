package com.increff.pos.dto;

import com.increff.pos.model.data.InvoiceData;
import com.increff.pos.model.data.InvoiceItem;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.model.enums.OrderStatus;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.ConversionUtil;
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
            ProductPojo productPojo = productService.getCheck(orderItemPojo.getProductId());
            InvoiceItem invoiceItem = ConversionUtil.convert(orderItemPojo, InvoiceItem.class);
            invoiceItem.setItemName(productPojo.getName());
            invoiceItem.setMrp(productPojo.getMrp());
            list2.add(invoiceItem);
        }
        InvoiceData invoiceData = convert(list2, orderId, orderDateTime);
        ResponseEntity<Resource> responseEntity = invoiceService.getInvoice(invoiceData);
        orderService.update(Integer.valueOf(invoiceData.getNumber()), OrderStatus.INVOICED);
        return responseEntity;
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

package com.increff.pos.dto;

import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.InvoiceItem;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceSenderDto {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    public InvoiceData getInvoiceItem(int orderId) throws ApiException {
        List<OrderItemPojo> list = orderItemService.getAll(orderId);
        List<InvoiceItem> list2 = new ArrayList<InvoiceItem>();
        for (OrderItemPojo orderItemPojo : list) {
            list2.add(convert(orderItemPojo));
        }
        InvoiceData invoiceData = new InvoiceData();
        invoiceData.setInvoiceItems(list2);
        invoiceData.setDate(orderService.get(orderId).getOrderTime().toLocalDate().toString());
        invoiceData.setNumber(String.valueOf(orderId));
        return invoiceData;
    }

    private InvoiceItem convert(OrderItemPojo orderItemPojo) throws ApiException {
        InvoiceItem invoiceItem = new InvoiceItem();
        ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
        invoiceItem.setItemName(productPojo.getProductName());
        invoiceItem.setQuantity(orderItemPojo.getProductQuantity());
        invoiceItem.setMrp(productPojo.getProductMrp());
        invoiceItem.setSellingPrice(orderItemPojo.getSellingPrice());
        return invoiceItem;
    }






}

package com.increff.pos.dto;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderItemDto {
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(OrderItemForm orderItemForm, int orderId) throws ApiException {
        NormalizeUtil.normalize(orderItemForm);
        ValidationUtil.checkValid(orderItemForm);
        ProductPojo productPojo = productService.getCheckBarcode(orderItemForm.getBarcode());
        double productMrp = productPojo.getMrp();
        int productId = productPojo.getId();
        checkSellingPrice(orderItemForm.getSellingPrice(), productMrp, productService.getCheck(productId).getBarcode());
        OrderItemPojo orderItemPojo = orderItemService.getOrderItem(productId,orderId);
        if(orderItemPojo != null){
            int updatedQuantity = orderItemPojo.getQuantity() + orderItemForm.getQuantity();
            inventoryService.updateInventory(productId, orderItemPojo.getQuantity(), updatedQuantity);
            orderItemPojo.setQuantity(updatedQuantity);
            orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
            return;
        }
        inventoryService.checkInputQuantity(productId, orderItemForm.getQuantity(), productService.getCheck(productId).getBarcode());
        orderItemPojo = ConversionUtil.convert(orderItemForm, OrderItemPojo.class);
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductId(productId);
        orderItemService.add(orderItemPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void delete(int id) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemService.getCheck(id);
        orderService.getCheckInvoicedStatus(orderItemPojo.getOrderId());
        orderService.getCheckCancelledStatus(orderItemPojo.getOrderId());
        orderItemService.delete(id);
        int productId = orderItemPojo.getProductId();
        InventoryPojo inventoryPojo = inventoryService.getCheck(productId);
        inventoryService.update(productId, inventoryPojo.getQuantity() + orderItemPojo.getQuantity());
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int itemId, OrderItemForm orderItemForm) throws ApiException {
        NormalizeUtil.normalize(orderItemForm);
        ValidationUtil.checkValid(orderItemForm);
        orderService.getCheckInvoicedStatus(orderItemService.getCheck(itemId).getOrderId());
        orderService.getCheckCancelledStatus(orderItemService.getCheck(itemId).getOrderId());
        int productId = orderItemService.getCheck(itemId).getProductId();
        Double productMrp = productService.getCheck(productId).getMrp();
        checkSellingPrice(orderItemForm.getSellingPrice(), productMrp, productService.getCheck(productId).getBarcode());
        inventoryService.updateInventory(productId, orderItemService.getCheck(itemId).getQuantity(), orderItemForm.getQuantity());
        orderItemService.update(itemId, orderItemForm.getSellingPrice(), orderItemForm.getQuantity());
    }

    public OrderItemData get(int id) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemService.getCheck(id);
        ProductPojo productPojo = productService.getCheck(orderItemPojo.getProductId());
        OrderItemData orderItemData = ConversionUtil.convert(orderItemPojo, OrderItemData.class);
        orderItemData.setBarcode(productPojo.getBarcode());
        orderItemData.setProductName(productPojo.getName());
        orderItemData.setProductMrp(productPojo.getMrp());
        return orderItemData;
    }

    public List<OrderItemData> getAll(int orderId) throws ApiException {
        List<OrderItemPojo> list = orderItemService.getAll(orderId);
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for (OrderItemPojo orderItemPojo : list) {
            ProductPojo productPojo = productService.getCheck(orderItemPojo.getProductId());
            OrderItemData orderItemData = ConversionUtil.convert(orderItemPojo, OrderItemData.class);
            orderItemData.setBarcode(productPojo.getBarcode());
            orderItemData.setProductName(productPojo.getName());
            orderItemData.setProductMrp(productPojo.getMrp());
            list2.add(orderItemData);
        }
        return list2;
    }

    //check function

    private void checkSellingPrice(double sellingPrice, double productMrp, String barcode) throws ApiException {
        if (productMrp < sellingPrice) {
            throw new ApiException("Selling price for "+barcode+" cannot be more than MRP!!  (MRP = " + productMrp + ")");
        }
    }

}

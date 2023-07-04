package com.increff.pos.dto;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.RollbackException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
    public void add() throws ApiException {
        List<CartPojo> list = cartService.getAll(getUser());
        if(list.isEmpty()){
            throw new ApiException("Empty list cannot be pushed");
        }
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setOrderTime(LocalDateTime.now());
        int orderId = orderService.add(orderPojo);
        for(CartPojo cartPojo : list){
            int productId = productService.getId(cartPojo.getProductBarcode());
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setProductId(productId);
            int productQuantity = inventoryService.get(productId).getProductQuantity();
            if(cartPojo.getProductQuantity() > productQuantity){
                throw new ApiException(productQuantity + " items are left for " + cartPojo.getProductBarcode());
            }
            if(cartPojo.getProductSP() > productService.get(productId).getProductMrp()){
                throw new ApiException("Selling price for " + cartPojo.getProductBarcode() + " is greater than Mrp");
            }
            int updatedQuantity = productQuantity - cartPojo.getProductQuantity();
            inventoryPojo.setProductQuantity(updatedQuantity);
            inventoryService.update(productId,inventoryPojo);
            orderItemService.add(convert(cartPojo,orderId,productId));
            cartService.delete(cartPojo.getItemNo());
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public void delete(int id) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemService.get(id);
        if(orderItemService.getAll(orderItemPojo.getOrderId()).size() == 1){
            throw new ApiException("Sorry! order cannot be empty");
        }
        int productId = orderItemPojo.getProductId();
        InventoryPojo inventoryPojo = inventoryService.get(productId);
        inventoryPojo.setProductQuantity(inventoryPojo.getProductQuantity() + orderItemPojo.getProductQuantity());
        orderItemService.delete(id);
    }

    public void update(int itemId, OrderItemForm orderItemForm) throws ApiException {
        OrderItemPojo orderItemPojo = convert(orderItemForm);
        checkInput(orderItemPojo);
        updateInventory(itemId, orderItemPojo);
        orderItemService.update(itemId, orderItemPojo);
    }

    public OrderItemData get(int id) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemService.get(id);
        return convert(orderItemPojo);
    }

    public List<OrderItemData> getAll(int orderId) throws ApiException {
        List<OrderItemPojo> list = orderItemService.getAll(orderId);
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for (OrderItemPojo orderItemPojo : list) {
            list2.add(convert(orderItemPojo));
        }
        return list2;
    }

    private void checkInput(OrderItemPojo orderItemPojo) throws ApiException {
        int productId = orderItemPojo.getProductId();
        double productMrp = productService.get(productId).getProductMrp();
        if (productMrp < orderItemPojo.getSellingPrice()) {
            throw new ApiException("Selling price cannot be more than MRP!!  (MRP = " + productMrp + " )");
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    private void updateInventory(int itemId , OrderItemPojo orderItemPojo) throws ApiException {
        int oldQuantity = orderItemService.get(itemId).getProductQuantity();
        int newQuantity = orderItemPojo.getProductQuantity();
        if(oldQuantity == newQuantity){
            return;
        }
        int productId = orderItemPojo.getProductId();
        InventoryPojo inventoryPojo = inventoryService.get(productId);
        int currentInventory = inventoryPojo.getProductQuantity();
        if(newQuantity < oldQuantity){
            inventoryPojo.setProductQuantity(oldQuantity-newQuantity+currentInventory);
            inventoryService.update(productId, inventoryPojo);
        }
        else if(newQuantity - oldQuantity < currentInventory) {
            inventoryPojo.setProductQuantity(currentInventory - newQuantity + oldQuantity);
            inventoryService.update(productId, inventoryPojo);
        }
        else
            throw new ApiException("Sorry! Available up to: " + (oldQuantity+ currentInventory));
    }

    private OrderItemData convert(OrderItemPojo orderItemPojo) throws ApiException {
        OrderItemData orderItemData = new OrderItemData();
        ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
        orderItemData.setOrderItemId(orderItemPojo.getOrderItemId());
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setProductId(orderItemPojo.getProductId());
        orderItemData.setProductBarcode(productPojo.getProductBarcode());
        orderItemData.setProductName(productPojo.getProductName());
        orderItemData.setProductQuantity(orderItemPojo.getProductQuantity());
        orderItemData.setProductMrp(productPojo.getProductMrp());
        orderItemData.setProductSP(orderItemPojo.getSellingPrice());
        return orderItemData;
    }

    private OrderItemPojo convert(OrderItemForm orderItemForm) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderItemId(orderItemForm.getOrderItemId());
        orderItemPojo.setOrderId(orderItemForm.getOrderId());
        orderItemPojo.setProductId(orderItemForm.getProductId());
        orderItemPojo.setProductQuantity(orderItemForm.getProductQuantity());
        orderItemPojo.setSellingPrice(orderItemForm.getProductSP());
        return  orderItemPojo;
    }

    private String getUser(){
        UserPrincipal principal = SecurityUtil.getPrincipal();
        return principal.getEmail();
    }

    private OrderItemPojo convert(CartPojo cartPojo, int orderId, int productId){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductQuantity(cartPojo.getProductQuantity());
        orderItemPojo.setSellingPrice(cartPojo.getProductSP());
        orderItemPojo.setProductId(productId);
        return  orderItemPojo;
    }

}

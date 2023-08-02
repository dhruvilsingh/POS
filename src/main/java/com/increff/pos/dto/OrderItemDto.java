package com.increff.pos.dto;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.forms.CartForm;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidationUtil;
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

    @Transactional
    public void add(OrderItemForm orderItemForm, int orderId) throws ApiException {
        normalize(orderItemForm);
        ValidationUtil.checkValid(orderItemForm);
        ProductPojo productPojo = productService.getCheckBarcode(orderItemForm.getBarcode());
        double productMrp = productPojo.getMrp();
        int productId = productPojo.getId();
        checkSellingPrice(orderItemForm.getSellingPrice(), productMrp, productService.get(productId).getBarcode());
        OrderItemPojo orderItemPojo = orderItemService.getCheckOrderItem(productId,orderId);
        if(orderItemPojo != null){
            int updatedQuantity = orderItemPojo.getQuantity() + orderItemForm.getQuantity();
            inventoryService.updateInventory(productId, orderItemPojo.getQuantity(), updatedQuantity);
            orderItemPojo.setQuantity(updatedQuantity);
            orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
            return;
        }
        inventoryService.checkInputQuantity(productId, orderItemForm.getQuantity(), productService.get(productId).getBarcode());
        orderItemService.add(convert(orderItemForm, orderId, productId));
    }

    @Transactional(rollbackOn = ApiException.class)
    public void delete(int id) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemService.get(id);
        orderService.getCheckInvoicedStatus(orderItemPojo.getOrderId());
        orderService.getCheckCancelledStatus(orderItemPojo.getOrderId());
        orderItemService.delete(id);
        int productId = orderItemPojo.getProductId();
        InventoryPojo inventoryPojo = inventoryService.get(productId);
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() + orderItemPojo.getQuantity());
        inventoryService.update(productId, inventoryPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int itemId, OrderItemForm orderItemForm) throws ApiException {
        normalize(orderItemForm);
        ValidationUtil.checkValid(orderItemForm);
        orderService.getCheckInvoicedStatus(orderItemService.get(itemId).getOrderId());
        orderService.getCheckCancelledStatus(orderItemService.get(itemId).getOrderId());
        int productId = orderItemService.get(itemId).getProductId();
        Double productMrp = productService.get(productId).getMrp();
        checkSellingPrice(orderItemForm.getSellingPrice(), productMrp, productService.get(productId).getBarcode());
        inventoryService.updateInventory(productId, orderItemService.get(itemId).getQuantity(), orderItemForm.getQuantity());
        orderItemService.update(itemId, convert(orderItemForm, orderItemService.get(itemId).getOrderId(), productId));
    }

    public OrderItemData get(int id) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemService.get(id);
        ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
        return convert(orderItemPojo, productPojo);
    }

    public List<OrderItemData> getAll(int orderId) throws ApiException {
        List<OrderItemPojo> list = orderItemService.getAll(orderId);
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for (OrderItemPojo orderItemPojo : list) {
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            list2.add(convert(orderItemPojo, productPojo));
        }
        return list2;
    }

    //check function

    private void checkSellingPrice(double sellingPrice, double productMrp, String barcode) throws ApiException {
        if (productMrp < sellingPrice) {
            throw new ApiException("Selling price for "+barcode+" cannot be more than MRP!!  (MRP = " + productMrp + ")");
        }
    }


    //convert functions

    private static OrderItemData convert(OrderItemPojo orderItemPojo, ProductPojo productPojo){
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setId(orderItemPojo.getId());
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setProductBarcode(productPojo.getBarcode());
        orderItemData.setProductName(productPojo.getName());
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setProductMrp(productPojo.getMrp());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        return orderItemData;
    }

    private OrderItemPojo convert(OrderItemForm orderItemForm, int orderId, int productId){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductId(productId);
        orderItemPojo.setQuantity((int) orderItemForm.getQuantity());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        return  orderItemPojo;
    }

    private static void normalize(OrderItemForm orderItemForm){
        orderItemForm.setSellingPrice(StringUtil.roundOff(orderItemForm.getSellingPrice()));
    }

}

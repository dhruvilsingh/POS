package com.increff.pos.dto;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.forms.OrderItemForm;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrdersPojo;
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
public class OrderDto {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackOn = ApiException.class)
    public void createOrder() throws ApiException {
        List<CartPojo> list = cartService.getAll(SecurityUtil.getUser());
        if(list.isEmpty()){
            throw new ApiException("Order empty!");
        }
        int orderId = orderService.add();
        for(CartPojo cartPojo : list){
            int productId = cartPojo.getProductId();
            Double productMrp = productService.get(productId).getMrp();
            checkSellingPrice(cartPojo.getSellingPrice(), productMrp, productService.get(productId).getBarcode());
            inventoryService.checkInputQuantity(productId, cartPojo.getQuantity(), productService.get(productId).getBarcode());
            orderItemService.add(convert(cartPojo, orderId));
        }
        cartService.deleteAll(SecurityUtil.getUser());
    }

    public OrderData get(int id) throws ApiException {
        OrdersPojo ordersPojo = orderService.getCheck(id);
        return convert(ordersPojo);
    }

    public List<OrderData> getAll() {
        List<OrdersPojo> list = orderService.getAll();
        List<OrderData> list2 = new ArrayList<OrderData>();
        for (OrdersPojo ordersPojo : list) {
            list2.add(convert(ordersPojo));
        }
        return list2;
    }

    public void cancelOrder(int orderId) throws ApiException {
        orderService.getCheckInvoicedStatus(orderId);
        List<OrderItemPojo> list = orderItemService.getAll(orderId);
        for(OrderItemPojo orderItemPojo : list){
            inventoryService.updateInventory(orderItemPojo.getProductId(), orderItemPojo.getQuantity(), 0);
        }
        orderService.cancelOrder(orderId);
    }

    private void checkSellingPrice(double sellingPrice, double productMrp, String barcode) throws ApiException {
        if (productMrp < sellingPrice) {
            throw new ApiException("Selling price for "+barcode+" cannot be more than MRP!!  (MRP = " + productMrp + ")");
        }
    }

    //convert functions
    private static OrderData convert(OrdersPojo ordersPojo) {
        OrderData orderData = new OrderData();
        orderData.setId(ordersPojo.getId());
        orderData.setStatus(ordersPojo.getStatus().toString());
        orderData.setTime(ordersPojo.getTime());
        return orderData;
    }

    private OrderItemPojo convert(CartPojo cartPojo, int orderId){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setQuantity(cartPojo.getQuantity());
        orderItemPojo.setSellingPrice(cartPojo.getSellingPrice());
        orderItemPojo.setProductId(cartPojo.getProductId());
        return  orderItemPojo;
    }

    private OrderItemPojo convert(OrderItemForm orderItemForm){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setQuantity((int) orderItemForm.getQuantity());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        return  orderItemPojo;
    }

}

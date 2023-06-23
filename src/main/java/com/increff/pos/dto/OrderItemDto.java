package com.increff.pos.dto;

import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    public void add() throws ApiException {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setOrderTime(LocalDateTime.now());
        int orderId = orderService.add(orderPojo);
        List<CartPojo> list = cartService.getAll(LoginDto.userEmail);
        for(CartPojo cartPojo : list){
            orderItemService.add(convert(cartPojo,orderId));
            cartService.delete(cartPojo.getItemNo());
        }
    }

    private OrderItemPojo convert(CartPojo cartPojo, int orderId){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductQuantity(cartPojo.getProductQuantity());
        orderItemPojo.setSellingPrice(cartPojo.getProductSP());
        orderItemPojo.setProductId(productService.getId(cartPojo.getProductBarcode()));
        return  orderItemPojo;
    }

}

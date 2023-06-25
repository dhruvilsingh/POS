package com.increff.pos.dto;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.*;
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

    public List<OrderItemData> getAll(int orderId) throws ApiException {
        List<OrderItemPojo> list = orderItemService.getAll(orderId);
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for (OrderItemPojo orderItemPojo : list) {
            list2.add(convert(orderItemPojo));
        }
        return list2;
    }

    private OrderItemData convert(OrderItemPojo orderItemPojo) throws ApiException {
        OrderItemData orderItemData = new OrderItemData();
        ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
        orderItemData.setProductBarcode(productPojo.getProductBarcode());
        orderItemData.setProductName(productPojo.getProductName());
        orderItemData.setProductQuantity(orderItemPojo.getProductQuantity());
        orderItemData.setProductMrp(productPojo.getProductMrp());
        orderItemData.setProductSP(orderItemPojo.getSellingPrice());
        return orderItemData;
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

package com.increff.pos.dto;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrdersPojo;
import com.increff.pos.service.*;
import com.increff.pos.service.exception.ApiException;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.SecurityUtil;
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
        List<CartPojo> list = cartService.getAll(SecurityUtil.getUserEmail());
        if(list.isEmpty()){
            throw new ApiException("Order empty!");
        }
        int orderId = orderService.add();
        for(CartPojo cartPojo : list){
            int productId = cartPojo.getProductId();
            Double productMrp = productService.getCheck(productId).getMrp();
            checkSellingPrice(cartPojo.getSellingPrice(), productMrp, productService.getCheck(productId).getBarcode());
            inventoryService.checkInputQuantity(productId, cartPojo.getQuantity(), productService.getCheck(productId).getBarcode());
            OrderItemPojo orderItemPojo = ConversionUtil.convert(cartPojo, OrderItemPojo.class);
            orderItemPojo.setOrderId(orderId);
            orderItemService.add(orderItemPojo);
        }
        cartService.deleteAll(SecurityUtil.getUserEmail());
    }

    public OrderData get(int id) throws ApiException {
        OrdersPojo ordersPojo = orderService.getCheck(id);
        return ConversionUtil.convert(ordersPojo, OrderData.class);
    }

    public List<OrderData> getAll() throws ApiException {
        List<OrdersPojo> list = orderService.getAll();
        List<OrderData> list2 = new ArrayList<OrderData>();
        for (OrdersPojo ordersPojo : list) {
            list2.add(ConversionUtil.convert(ordersPojo, OrderData.class));
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


}

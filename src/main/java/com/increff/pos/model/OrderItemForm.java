package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemForm {
    private int orderItemId;
    private int orderId;
    private int productId;
    private int productQuantity;
    private double productSP;
}

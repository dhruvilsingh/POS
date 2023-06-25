package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData{
    private String productBarcode;
    private String productName;
    private int productQuantity;
    private double productSP;
    private double productMrp;
}

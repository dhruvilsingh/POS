package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData extends OrderItemForm{
    private String productBarcode;
    private String productName;
    private double productMrp;
}

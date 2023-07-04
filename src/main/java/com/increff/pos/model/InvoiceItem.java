package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItem {
    private String itemName;
    private int quantity;
    private double sellingPrice;
    private double mrp;
}

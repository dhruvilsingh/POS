package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItem {
    private String itemName;
    private Integer quantity;
    private Double sellingPrice;
    private Double mrp;
}

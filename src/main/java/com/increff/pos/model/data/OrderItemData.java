package com.increff.pos.model.data;

import com.increff.pos.model.forms.OrderItemForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData extends OrderItemForm {
    private Integer id;
    private Integer orderId;
    private String productBarcode;
    private String productName;
    private Double productMrp;
}

package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartForm{
    String productBarcode;
    int productQuantity;
    double productSP;
}

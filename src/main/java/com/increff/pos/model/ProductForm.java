package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {
    private String productBarcode;
    private String productName;
    private double productMrp;
    private String productBrand;
    private String productCategory;
}

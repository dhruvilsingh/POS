package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleReportData{
    private int quantity;
    private double revenue;
    private String brandName;
    private String category;
}

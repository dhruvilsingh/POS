package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SaleReportData{
    private String brand;
    private String category;
    private Integer quantity;
    private Double revenue;
}

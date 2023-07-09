package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SaleReportForm {
    private String startDate;
    private String endDate;
    private String brandName;
    private String category;
}

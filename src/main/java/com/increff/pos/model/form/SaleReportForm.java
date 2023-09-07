package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class SaleReportForm {
    @NotBlank(message = "Start date cannot be empty!")
    private String startDate;
    @NotBlank(message = "End date cannot be empty!")
    private String endDate;
    private String brand;
    private String category;
}

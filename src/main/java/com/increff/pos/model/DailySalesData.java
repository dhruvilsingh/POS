package com.increff.pos.model;

import com.increff.pos.pojo.DailySalesPojo;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class DailySalesData{
    private LocalDate date;
    private int orderCount;
    private int orderItemCount;
    private double revenue;
}

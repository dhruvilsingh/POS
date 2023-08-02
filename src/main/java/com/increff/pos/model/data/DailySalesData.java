package com.increff.pos.model.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Setter
public class DailySalesData{
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private ZonedDateTime date;
    private Integer orderCount;
    private Integer orderItemCount;
    private Double revenue;
}

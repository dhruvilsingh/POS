package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;


@Getter
@Setter
@Entity
public class DailySalesPojo{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;
    private LocalDate date;
    private int orderCount;
    private int orderItemCount;
    private double revenue;
}

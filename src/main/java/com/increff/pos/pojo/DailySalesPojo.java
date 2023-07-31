package com.increff.pos.pojo;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;


@Getter
@Setter
@Entity
public class DailySalesPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    private ZonedDateTime date;

    @Column(nullable = false)
    private Integer orderCount;

    @Column(nullable = false)
    private Integer orderItemCount;

    @Column(nullable = false)
    private Double revenue;
}

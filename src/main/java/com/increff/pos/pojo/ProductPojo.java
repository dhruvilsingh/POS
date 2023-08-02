package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class ProductPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Double mrp;

    @Column(nullable = false)
    private Integer brandCategoryId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String barcode;
}

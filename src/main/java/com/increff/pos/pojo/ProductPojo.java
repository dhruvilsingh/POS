package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class ProductPojo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int productId;
    private double productMrp;
    private int productBrandCategory;
    private String productName;
    @Column(unique = true)
    private String productBarcode;
}

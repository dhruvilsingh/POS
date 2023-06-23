package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class CartPojo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int itemNo;
    private String userEmail;
    private String productBarcode;
    private int productQuantity;
    private double productSP;
}

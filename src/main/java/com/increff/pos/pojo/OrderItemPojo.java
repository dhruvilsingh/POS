package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class OrderItemPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator_1")
    @SequenceGenerator(name = "sequence_generator_1", sequenceName = "sequence_1", initialValue = 100001, allocationSize = 1)
    private int orderItemId;
    private int orderId;
    private int productId;
    private int productQuantity;
    private double sellingPrice;
}
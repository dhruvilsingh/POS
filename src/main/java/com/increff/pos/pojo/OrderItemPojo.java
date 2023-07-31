package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class OrderItemPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator_1")
    @SequenceGenerator(name = "sequence_generator_1", sequenceName = "sequence_1", initialValue = 100001, allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private Integer orderId;

    @Column(nullable = false)
    private Integer productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double sellingPrice;
}
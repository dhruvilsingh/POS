package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class OrderPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator_2")
    @SequenceGenerator(name = "sequence_generator_2", sequenceName = "sequence_2", initialValue = 1001, allocationSize = 1)
    private int orderId;
    private LocalDateTime orderTime;
}

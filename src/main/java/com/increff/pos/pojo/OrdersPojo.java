package com.increff.pos.pojo;

import com.increff.pos.util.StringUtil.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
public class OrdersPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator_2")
    @SequenceGenerator(name = "sequence_generator_2", sequenceName = "sequence_2", initialValue = 1001, allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private ZonedDateTime time;
}

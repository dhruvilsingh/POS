package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderData {
    int orderId;
    private LocalDateTime orderTime;
}

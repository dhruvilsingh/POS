package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class InventoryPojo {
    @Id
    private int productId;
    private int productQuantity;
}

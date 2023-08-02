package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;

@Getter
@Setter
public class CartForm extends OrderItemForm{
}

package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;

@Getter
@Setter
public class InventoryForm {

    @NotBlank(message = "Barcode cannot be empty")
    @Pattern(regexp = "^$|^[\\p{L}0-9\\-\\.\\$\\/\\+\\%\\s]+$", message = "Invalid characters in barcode")
    @Size(max = 255, message = "Barcode must have less than 255 characters")
    private String barcode;

    @Min(value = 0, message = "Quantity cannot be negative")
    @Digits(integer = 10, fraction = 0, message = "Quantity should be a natural number less than 100000")
    @Max(value = 2147483647, message = "Quantity should be less than or equal to " + Integer.MAX_VALUE)
    private Integer quantity;
}

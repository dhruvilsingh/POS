package com.increff.pos.model.forms;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;

@Getter
@Setter
public class ProductForm {

    @NotBlank(message = "Barcode cannot be empty")
    @Pattern(regexp = "^$|^[\\p{L}0-9\\-\\.\\$\\/\\+\\%\\s]+$", message = "Invalid characters in barcode")
    @Size(max = 255, message = "Barcode must have less than 255 characters")
    private String barcode;

    @NotBlank(message = "Product name cannot be empty")
    @Pattern(regexp = "^$|^[\\p{Ll}\\p{Lu}&@$£€¥0-9.,:;\\-\\p{P}\\p{S}\\s&&[^*+=#%]]+$", message = "Invalid characters in product name")
    @Size(max = 255, message = "Product name must have less than 255 characters")
    private String name;

    //    @Max(value = 1000000 ,message = "MRP should be less than 1000000")
    @DecimalMin(value = "0", message = "MRP cannot be negative")
    @NotNull(message = "MRP cannot be empty!")
    private Double mrp;

    @NotBlank(message = "Brand name cannot be empty")
    @Pattern(regexp = "^$|^[\\p{Ll}\\p{Lu}&@$£€¥0-9.,:;\\-\\p{P}\\p{S}\\s&&[^*+=#%]]+$", message = "Invalid characters in brand name")
    @Size(max = 255, message = "Brand name must have less than 255 characters")
    private String brand;

    @NotBlank(message = "Category cannot be empty")
    @Pattern(regexp = "^$|^[\\p{Ll}\\p{Lu}&@$£€¥0-9.,:;\\-\\p{P}\\p{S}\\s&&[^*+=#%]]+$", message = "Invalid characters in category")
    @Size(max = 255, message = "Category must have less than 255 characters")
    private String category;
}

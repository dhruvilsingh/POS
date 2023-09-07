package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BrandForm {

    @NotBlank(message = "Brand name cannot be empty")
    @Pattern(regexp = "^$|^[\\p{Ll}\\p{Lu}&@$£€¥0-9.,:;\\-\\p{P}\\p{S}\\s&&[^*+=#%]]+$", message = "Invalid characters in brand name")
    @Size(max = 255, message = "Brand name must have less than 255 characters")
    private String brand;

    @NotBlank(message = "Category cannot be empty")
    @Pattern(regexp = "^$|^[\\p{Ll}\\p{Lu}&@$£€¥0-9.,:;\\-\\p{P}\\p{S}\\s&&[^*+=#%]]+$", message = "Invalid characters in category")
    @Size(max = 255, message = "Category must have less than 255 characters")
    private String category;
}

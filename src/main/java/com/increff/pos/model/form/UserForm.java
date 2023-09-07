package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserForm {

	@NotBlank(message = "Email cannot be empty")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Please enter a valid email")
	@Size(max = 255, message = "Email must have less than 255 characters")
	private String email;

	@Size(min=4, max=20 , message = "Password must have between 4 to 20 characters")
	@NotBlank(message = "Password cannot be empty")
	private String password;
}

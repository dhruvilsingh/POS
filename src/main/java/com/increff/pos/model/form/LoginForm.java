package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginForm {

	@Email(message = "Please enter correct email format")
	@NotBlank(message = "Email cannot be empty")
	@Size(max = 255, message = "Email must have less than 255 characters")
	private String email;

	@Size(min=8, max=20 , message = "Password must have between 8 to 20 characters")
	@NotBlank(message = "Password cannot be empty")
	private String password;
}

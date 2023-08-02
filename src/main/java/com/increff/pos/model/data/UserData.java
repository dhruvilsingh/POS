package com.increff.pos.model.data;

import com.increff.pos.model.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {
	private Integer id;
	private String email;
	private Role role;
}

package com.increff.pos.controller;

import java.util.List;
import com.increff.pos.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.forms.UserForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class UserApiController {

	@Autowired
	private UserDto userDto;

	@ApiOperation(value = "Adds a user")
	@RequestMapping(path = "/api/users", method = RequestMethod.POST)
	public void addUser(@RequestBody UserForm form) throws ApiException {
		userDto.addUser(form);
	}

	@ApiOperation(value = "Deletes a user")
	@RequestMapping(path = "/api/users/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable int id) throws ApiException {
		userDto.deleteUser(id);
	}

	@ApiOperation(value = "Gets list of all users")
	@RequestMapping(path = "/api/users", method = RequestMethod.GET)
	public List<UserData> getAllUser() {
		return userDto.getAllUser();
	}
}

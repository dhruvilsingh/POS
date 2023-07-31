package com.increff.pos.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.increff.pos.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.forms.LoginForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.ApiOperation;

@Controller
public class LoginController {

	@Autowired
	private LoginDto loginDto;
	
	@ApiOperation(value = "Logs in a user")
	@RequestMapping(path = "/session/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView login(HttpServletRequest req, LoginForm f) throws ApiException {
		return loginDto.login(req, f);
	}

	@RequestMapping(path = "/session/init", method = RequestMethod.POST)
	public ModelAndView signUp(HttpServletRequest req, LoginForm f) throws ApiException {
		return loginDto.signUp(req, f);
	}


	@RequestMapping(path = "/session/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		return loginDto.logout(request, response);
	}
}

//package com.increff.pos.controller;
//
//import java.util.List;
//import com.increff.pos.dto.AbstractUiDto;
//import com.increff.pos.model.enums.Role;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//import com.increff.pos.model.data.InfoData;
//import com.increff.pos.model.forms.UserForm;
//import com.increff.pos.pojo.UserPojo;
//import com.increff.pos.service.ApiException;
//import com.increff.pos.service.UserService;
//import io.swagger.annotations.ApiOperation;
//
//@Controller
//public class InitApiController extends AbstractUiDto {
//
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private InfoData info;
//
//	@ApiOperation(value = "Initializes application")
//	@RequestMapping(path = "/site/init", method = RequestMethod.POST)
//	public ModelAndView initSite(UserForm form) throws ApiException {
//		List<UserPojo> list = userService.getAll();
//		if (list.size() > 0) {
//			info.setMessage("Application already initialized. Please login using existing credentials");
//		} else {
//			UserPojo p = convert(form);
//			userService.add(p);
//			info.setMessage("Application initialized! Please login");
//			return mav("login.html");
//		}
//		return mav("init.html");
//	}
//
//	private static UserPojo convert(UserForm f) {
//		UserPojo p = new UserPojo();
//		p.setEmail(f.getEmail());
//		p.setRole(Role.ADMIN);
//		p.setPassword(f.getPassword());
//		return p;
//	}
//
//}

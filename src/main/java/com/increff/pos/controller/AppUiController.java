package com.increff.pos.controller;

import com.increff.pos.dto.AbstractUiDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiDto {

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/ui/brand")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/ui/product")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/ui/createorder")
	public ModelAndView createOrder() {
		return mav("createorder.html");
	}

	@RequestMapping(value = "/ui/orderitem")
	public ModelAndView orderItem() {
		return mav("orderitem.html");
	}

	@RequestMapping(value = "/ui/order")
	public ModelAndView order() {
		return mav("orders.html");
	}

	@RequestMapping(value = "/ui/admin")
	public ModelAndView admin() {
		return mav("user.html");
	}

}

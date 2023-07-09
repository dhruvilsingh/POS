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

	@RequestMapping(value = "/ui/cart")
	public ModelAndView cart() {
		return mav("cart.html");
	}

	@RequestMapping(value = "/ui/orderitem")
	public ModelAndView orderItem() {
		return mav("orderitem.html");
	}

	@RequestMapping(value = "/ui/orders")
	public ModelAndView order() {
		return mav("orders.html");
	}

	@RequestMapping(value = "/ui/dailysales")
	public ModelAndView dailySales() {
		return mav("dailysales.html");
	}

	@RequestMapping(value = "/ui/brandreport")
	public ModelAndView brandReport() {
		return mav("brandreport.html");
	}

	@RequestMapping(value = "/ui/inventoryreport")
	public ModelAndView inventoryReport() {
		return mav("inventoryreport.html");
	}

	@RequestMapping(value = "/ui/salereport")
	public ModelAndView saleReport() {
		return mav("salereport.html");
	}

	@RequestMapping(value = "/ui/user")
	public ModelAndView admin() {
		return mav("user.html");
	}

}

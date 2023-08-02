package com.increff.pos.controller;

import com.increff.pos.dto.AbstractUiDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ui")
public class AppUiController extends AbstractUiDto {

	@RequestMapping("/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping("/brand")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping("/product")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping("/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping("/orders")
	public ModelAndView order() {
		return mav("orders.html");
	}

	@RequestMapping("/dailysales")
	public ModelAndView dailySales() {
		return mav("dailysales.html");
	}

	@RequestMapping("/brandreport")
	public ModelAndView brandReport() {
		return mav("brandreport.html");
	}

	@RequestMapping("/inventoryreport")
	public ModelAndView inventoryReport() {
		return mav("inventoryreport.html");
	}

	@RequestMapping("/salereport")
	public ModelAndView saleReport() {
		return mav("salereport.html");
	}

	@RequestMapping("/user")
	public ModelAndView admin() {
		return mav("user.html");
	}

}

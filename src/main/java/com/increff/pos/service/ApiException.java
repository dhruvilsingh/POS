package com.increff.pos.service;

import com.increff.pos.model.InfoData;
import org.springframework.beans.factory.annotation.Autowired;

public class ApiException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ApiException(String string) {
		super(string);
	}

}

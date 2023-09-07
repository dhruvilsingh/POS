package com.increff.pos.service.exception;

import java.util.List;
import java.util.Map;

public class ApiException extends Exception {
	private List<Map<String,String>> errorList;
	
	public ApiException(String string) {
		super(string);
	}

	public ApiException(String message, List<Map<String,String>> errorList) {
		super(message);
		this.errorList = errorList;
	}

	public List<Map<String,String>> getErrorList() {
		return errorList;
	}
}

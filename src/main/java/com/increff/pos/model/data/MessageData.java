package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MessageData {
	private String message;
	private List<Map<String,String>> errors;
}

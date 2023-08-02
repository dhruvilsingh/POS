package com.increff.pos.util;

import java.text.DecimalFormat;

public class StringUtil {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String toLowerCase(String s) {
		return s == null ? null : s.trim().toLowerCase();
	}

	public static double roundOff(double number){
		DecimalFormat df = new DecimalFormat("#.##");
		double roundedNumber = Double.parseDouble(df.format(number));
		return roundedNumber;
	}

}

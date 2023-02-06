package com.increff.pos.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StringUtil {


	public static String toLowerCase(String s) {
		return s == null ? null : s.trim().toLowerCase();
	}

	public static Double round(Double value, Integer places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}

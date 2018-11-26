package com.site.visits.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	public static String getDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		return sdf.format(date);
	}

	public static String getTimestamp() {
		SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormatUTC.format(new Date());

	}
}

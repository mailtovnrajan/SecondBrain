package com.example.instantjournal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
	public static String getCurrentTimestamp() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		return dateFormat.format(new Date());
	}
}

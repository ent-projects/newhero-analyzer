/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package com.newhero.utils;

import java.sql.Timestamp;



/**
 * @author wuheng09@gmail.com
 *
 */
public class ConvertorUtils {

	@SuppressWarnings("deprecation")
	public static Timestamp toTimestamp(String str) {
		String[] values = str.split("T");
		String[] splits = values[0].split("-");
		return new Timestamp(
				Integer.parseInt(splits[0].trim()) - 1900, 
				Integer.parseInt(splits[1].trim()), 
				Integer.parseInt(splits[2].trim()), 
				0, 0, 0, 0);
	}
}

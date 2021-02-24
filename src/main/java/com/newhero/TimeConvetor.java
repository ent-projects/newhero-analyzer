/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package com.newhero;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wuheng09@gmail.com
 *
 */
public class TimeConvetor {

	public static void main(String[] args) throws Exception {
		String time = "2020-12-24";
		Date start = new SimpleDateFormat("yyyy-MM-dd").parse(time);
		System.out.println(getDayDiffer(start, new Date()));
	}

	
	public static int getDayDiffer(Date startDate, Date endDate) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long startDateTime = dateFormat.parse(dateFormat.format(startDate)).getTime();
        long endDateTime = dateFormat.parse(dateFormat.format(endDate)).getTime();
        return (int) ((endDateTime - startDateTime) / (1000 * 3600 * 24));
    }
}

package org.rubik.util.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	
	public static final int DAY_SECONDS						= 86400;
	public static final int MINUTE_SECONDS					= 60;
	
	public static final String yyyyMMddHHmmss			= "yyyyMMddHHmmss";
	public static final String YYYY_MM_DD_HH_MM_SS		= "yyyy-MM-dd HH:mm:ss";
	
	public static final String ISO8601_UTC 				= "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String ISO8601_UTC_S 			= "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	public static final TimeZone TIMEZONE_UTC			= TimeZone.getTimeZone("UTC");
	public static final TimeZone TIMEZONE_GMT_8			= TimeZone.getTimeZone("GMT+8:00");
	
	// *************************** 获取时间戳  *************************** 
	
	/**
	 * 获取系统当前时间戳(秒)
	 */
	public static int current() {
		return (int) (System.currentTimeMillis() / 1000);
	}
	
	/**
	 * 使用默认时区将日期转换为时间戳(毫秒)
	 */
	public static final long getTime(String date, String format) { 
		return getTime(date, format, TIMEZONE_GMT_8);
	}
	
	/**
	 * 使用指定时区将日期转换为时间戳(毫秒)
	 */
	public static final long getTime(String date, String format, TimeZone zone) { 
		DateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(zone);
		try {
			return df.parse(date).getTime();
		} catch (ParseException e) {
			throw new RuntimeException("Date - " + date + " parse failure for fomat [" + format + "]");
		}
	}
	
	// *************************** 时间格式化  *************************** 
	
	/**
	 * iso8601 格林威治时间
	 */
	public static final String iso8601UTCDate() { 
		return getDate(System.currentTimeMillis(), ISO8601_UTC, TIMEZONE_UTC);
	}
	
	/**
	 * 由于iso8601只精确到秒，因此这里做了一个变种，该方法的格式类似：2017-10-12T10:01:46.818Z
	 */
	public static final String iso8601UTCMillisDate() {
		return getDate(System.currentTimeMillis(), ISO8601_UTC_S, TIMEZONE_UTC);
	}
	
	/**
	 * 使用默认时区获取指定时间格式的系统当前时间
	 */
	public static final String getDate(String format) {
		return getDate(System.currentTimeMillis(), format);
	}
	
	/**
	 * 使用默认时区将时间转换为指定的时间格式
	 */
	public static final String getDate(Date date, String format) {
		return getDate(date, format, TIMEZONE_GMT_8);
	}
	
	/**
	 * 使用默认时区将unix时间戳转换为指定时间格式的时间
	 */
	public static final String getDate(long timestamp, String format) {
		return getDate(timestamp, format, TIMEZONE_GMT_8);
	}
	
	/**
	 * 使用指定时区将时间转换为指定的时间格式
	 */
	public static final String getDate(Date date, String format, TimeZone timeZone) { 
		DateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(timeZone);
		return df.format(date);
	}
	
	/**
	 * 使用指定时区将unix时间戳转换为指定时间格式的时间
	 */
	public static final String getDate(long timestamp, String format, TimeZone timeZone) {
		DateFormat df = new SimpleDateFormat(format.toString());
		df.setTimeZone(timeZone);
		return df.format(new Date(timestamp));
	}
	
	/**
	 * 使用默认时区将 format 格式的时间 date转换为toFormat时间格式的时间
	 */
	public static final String getDate(String date, String format, String toFormat) {
		return getDate(date, format, toFormat, TIMEZONE_GMT_8);
	}
	
	/**
	 * 使用指定时区将 format 格式的时间 date转换为toFormat时间格式的时间
	 */
	public static final String getDate(String date, String format, String toFormat, TimeZone timeZone) {
		long time = getTime(date, format, timeZone);
		return getDate(time, toFormat);
	}
	
	// *************************** 零点时间  *************************** 
	
	/**
	 * 获取零点时间(秒)
	 * 
	 * @param time
	 * @return
	 */
	public static final int zeroTime(int time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(time * 1000l));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return (int) (calendar.getTimeInMillis() / 1000);
	}
	
	/**
	 * 获取零点时间(毫秒)
	 * 
	 * @param time
	 * @return
	 */
	public static final long zeroTime(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(time));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
}

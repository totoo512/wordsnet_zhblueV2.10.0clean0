package cn.edu.zjicm.zhengjun.words.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {

	public static java.text.DateFormat df = new SimpleDateFormat("yyyyMMdd");
	public static java.text.DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
	public static java.text.DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final int MINUTES_OF_DAY = 1440; // 24*60
	public static final int SECONDS_OF_DAY = 86400; // 24*60*60
	public static final int MICROSECONDS_OF_DAY = 86400000; // 24*60*60*1000

	// public static final int STUDY_CYCLE = 15; //单词的学习周期为15天

	/* 日期和时间 */
	public static int getDate() {
		Date now = new Date();
		return Integer.parseInt(df.format(now));
	}

	/* 得到当前日期，考虑时区 */
	public static int getDateWithZoneRawOffset() {
		Date now = new Date();
		// LogUtil.e("date3",(int)
		// ((now.getTime()+TimeZone.getDefault().getRawOffset()) /
		// MICROSECONDS_OF_DAY) + "");
		return (int) ((now.getTime() + TimeZone.getDefault().getRawOffset()) / MICROSECONDS_OF_DAY);
	}

	// 得到当前时间，精确到分钟(已废除，需考虑时区，改用getTimeOfMinuteWithZoneRawOffsetTime())
	public static int getTime() {
		Date now = new Date();
		return (int) (now.getTime() / 1000 / 60);
	}

	// 得到当前时间，精确到分钟，考虑时区
	public static int getTimeOfMinuteWithZoneRawOffsetTime() {
		Date now = new Date();
		return (int) ((now.getTime() + TimeZone.getDefault().getRawOffset()) / 1000 / 60);
	}

	// 得到当前时间，精确秒(已废除，需考虑时区，改用getTimeOfSecondWithZoneRawOffsetTime())
	public static int getTimeOfSecond() {
		Date now = new Date();
		return (int) (now.getTime() / 1000);
	}

	// 得到当前时间，精确秒，考虑时区
	public static int getTimeOfSecondWithZoneRawOffsetTime() {
		Date now = new Date();
		return (int) ((now.getTime() + TimeZone.getDefault().getRawOffset()) / 1000);
	}

	public static String getTimeStr(long time) {
		Date d = new Date(time);
		return df3.format(d);
	}

	public static int getDaysBeforeToday(String dateStr) {
		long days = 0;
		if (dateStr.equals("0"))
			dateStr = "00000000";
		try {
			Date date = df.parse(dateStr);
			Date now = df.parse(df.format(new Date()));
			days = now.getTime() - date.getTime();
			days = days / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (int) days;
	}

	public static int getDate2() {
		Date now = new Date();
		return Integer.parseInt(df2.format(now));
	}


}

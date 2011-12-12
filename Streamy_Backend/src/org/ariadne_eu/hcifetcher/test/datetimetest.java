package org.ariadne_eu.hcifetcher.test;

import java.util.Locale;

import org.ariadne_eu.hcifetcher.util.DateManager;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class datetimetest {
	
	public static DateTimeFormatter fmt1 = new DateTimeFormatterBuilder()
			.appendDayOfMonth(2).appendLiteral(" ")
			.appendMonthOfYearShortText().appendLiteral(" ")
			.appendYear(2, 4).appendLiteral(" ")
			.appendHourOfDay(2).appendLiteral(":")
			.appendMinuteOfHour(2).appendLiteral(":")
			.appendSecondOfMinute(2).appendLiteral(" ")
			.appendTimeZoneOffset(null, false, 4,4).toFormatter().withLocale(Locale.ENGLISH); //13 Oct 2011 14:33:20 +0000"

	public static void main(String[] args) {
		String time = "Thu, 13 Oct 2011 14:33:20 +0000";
		System.out.println(time);
		time = time.split(", ")[1];
		System.out.println(time);
		DateManager.fmt1.parseDateTime(time).toDate();
		
	}
}

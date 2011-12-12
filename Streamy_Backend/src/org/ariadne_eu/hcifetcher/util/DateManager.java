package org.ariadne_eu.hcifetcher.util;

import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public class DateManager {
	
	
	protected static DateManager instance = null;
	
	public static DateTimeFormatter fmt1 = new DateTimeFormatterBuilder()
	.appendDayOfMonth(2).appendLiteral(" ")
	.appendMonthOfYearShortText().appendLiteral(" ")
	.appendYear(2, 4).appendLiteral(" ")
	.appendHourOfDay(2).appendLiteral(":")
	.appendMinuteOfHour(2).appendLiteral(":")
	.appendSecondOfMinute(2).appendLiteral(" ")
	.appendTimeZoneOffset(null, false, 2,2).toFormatter().withLocale(Locale.ENGLISH); //13 Oct 2011 14:33:20 +0000"
	
	//Tue Oct 04 19:17:05 UTC 2011
	
	protected DateManager() {
		
	}

	public static DateManager getInstance() {
		if(instance == null) instance = new DateManager();
		return instance;
	}
	
	public boolean isMoreRecent(DateTime timestamp, String type) {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("LastUpdated").addFilter("type", Query.FilterOperator.EQUAL, type);
		Entity result = datastore.prepare(query).asSingleEntity();
		if(result == null) return true;
		Date lastUpdated = ((Date)result.getProperty("date"));
		if(timestamp.isAfter(new DateTime(lastUpdated))) return true;
		return false;
	}
	
	public void updateTo(DateTime timestamp, String type) {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("LastUpdated").addFilter("type", Query.FilterOperator.EQUAL, type);
		Entity result = datastore.prepare(query).asSingleEntity();
		if(result != null) {
			result.setProperty("date", timestamp.toDate());	
		} else {
			result = new Entity("LastUpdated");
			result.setProperty("type", type);
			result.setProperty("date", timestamp.toDate());
		}		

		datastore.put(result);
	}
	
}

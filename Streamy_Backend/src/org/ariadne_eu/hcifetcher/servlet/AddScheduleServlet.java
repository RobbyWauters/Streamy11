package org.ariadne_eu.hcifetcher.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ariadne_eu.hcifetcher.util.DateManager;
import org.ariadne_eu.hcifetcher.util.twitter.TwitterSearch;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;

public class AddScheduleServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(AddScheduleServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//log.log(Level.INFO, "Cron job");
		createScheduleEntities();
	}

	private void createScheduleEntities(){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		log.log(Level.INFO, "createScheduleEntities");
		
		Date dateStamp = new Date();
		
		Entity scheduleEntity = new Entity("Schedule");

		scheduleEntity.setProperty("set", "");
		scheduleEntity.setProperty("guid", ""+Math.random()*1333337);
		scheduleEntity.setProperty("datestamp", dateStamp);
		scheduleEntity.setProperty("author", "Erik Duval");
		scheduleEntity.setProperty("course", "Multimedia");
		scheduleEntity.setProperty("text", new Text("Les van vrijdag 24/12/2011 valt weg."));
		//entryEntity.setProperty("tags", "");
		//entryEntity.setProperty("link", "");
		scheduleEntity.setProperty("type", "Schedule");
		scheduleEntity.setProperty("lastmodified","");
		
		datastore.put(scheduleEntity);
		
		//put new object in ENTRY
		Entity entryEntity = new Entity("Entry");
		
		entryEntity.setProperty("set", "");
		entryEntity.setProperty("guid", ""+Math.random()*1333337);
		entryEntity.setProperty("datestamp", dateStamp);
		entryEntity.setProperty("author", "Erik Duval");
		entryEntity.setProperty("course", "Multimedia");
		entryEntity.setProperty("text", new Text("Les van vrijdag 24/12/2011 valt weg."));
		//entryEntity.setProperty("tags", "");
		//entryEntity.setProperty("link", "");
		entryEntity.setProperty("type", "Schedule");
		entryEntity.setProperty("lastmodified","");
		
		datastore.put(entryEntity);
		
	}
	

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}

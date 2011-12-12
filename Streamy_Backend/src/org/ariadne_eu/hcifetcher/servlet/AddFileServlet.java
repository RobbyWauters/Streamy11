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

public class AddFileServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(AddFileServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.log(Level.INFO, "Cron job");
		
		createFileEntities();
	}

	private void createFileEntities(){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		log.log(Level.INFO, "createFileEntities");
		
		//vars
		Date dateStamp = new Date();
		
		Entity fileEntity = new Entity("File");

		fileEntity.setProperty("set", "");
		fileEntity.setProperty("guid", "00000000001");
		fileEntity.setProperty("datestamp", dateStamp);
		fileEntity.setProperty("author", "Erik Duval");
		fileEntity.setProperty("text", new Text("Nieuwe richtlijnen voor thesis in HCI groep staan online."));
		//entryEntity.setProperty("tags", "");
		fileEntity.setProperty("course", "Multimedia");
		fileEntity.setProperty("breadcrumb", "docs/general/scan.pdf");
		fileEntity.setProperty("link", "http://rwd.snakeflash.com/Demos/scan.pdf");
		fileEntity.setProperty("type", "File");
		fileEntity.setProperty("lastmodified","");
		
		datastore.put(fileEntity);
		
		//put new object in ENTRY
		Entity entryEntity = new Entity("Entry");

		entryEntity.setProperty("set", "");
		entryEntity.setProperty("guid", "00000000001");
		entryEntity.setProperty("datestamp", dateStamp);
		entryEntity.setProperty("author", "Erik Duval");
		entryEntity.setProperty("text", new Text("Nieuwe richtlijnen voor thesis in HCI groep staan online."));
		//entryEntity.setProperty("tags", "");
		entryEntity.setProperty("course", "Multimedia");
		entryEntity.setProperty("breadcrumb", "docs/general/scan.pdf");
		entryEntity.setProperty("link", "http://rwd.snakeflash.com/Demos/scan.pdf");
		entryEntity.setProperty("type", "File");
		entryEntity.setProperty("lastmodified","");
		
		datastore.put(entryEntity);
	}
	

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}

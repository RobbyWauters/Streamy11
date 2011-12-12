package org.ariadne_eu.hcifetcher.rest;

import it.sauronsoftware.feed4j.bean.FeedItem;
import it.sauronsoftware.feed4j.bean.RawElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tools.ant.types.LogLevel;
import org.ariadne_eu.hcifetcher.util.DateManager;
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
import com.google.appengine.api.datastore.Query.SortDirection;

public class RestUtils {
	
	protected static RestUtils instance = null;

	private static final Logger log = Logger.getLogger(RestUtils.class.getName());
	
	protected RestUtils() {
		
	}

	public static RestUtils getInstance() {
		if(instance == null) instance = new RestUtils();
		return instance;
	}
	
	public String getEntries(String type, String set, String author) {
		JSONObject json = new JSONObject();

		JSONArray results = new JSONArray(getEntriesJSON(type, set, author,null));
		
		try {
			json.put("total_results", results.length());
			json.put("results", results);
		} catch (JSONException e) {
			log.log(Level.SEVERE,"JSONException",e);
		}
		
		return json.toString();
	}
	
	public Vector<JSONObject> getEntriesJSON(String type, String set, String author, String formatStr) {
		return getEntriesJSON(type, set, author, formatStr, 0, 100);
	}
	
	// To export
	public Vector<JSONObject> getEntriesJSON(String type, String set, String author, String formatStr, int offset, int size) {
		Vector<JSONObject> results = new Vector<JSONObject>();
		if(offset < 0 || size < 1)  return results;
		boolean export = false;
		if(formatStr != null && formatStr.equalsIgnoreCase("export")) {
			export = true;
		}
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query query = null;
			if(type == null) {
				query = new Query();
			}else {
				query = new Query(type);	
			}			
			if(set != null && !set.trim().equals("")) {
				query.addFilter("set", Query.FilterOperator.EQUAL, set);
			}
			if(author != null && !author.trim().equals("")) {
				query.addFilter("author", Query.FilterOperator.EQUAL, author);
			}
			query.addSort("datestamp", SortDirection.DESCENDING);
			List<Entity> result = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(size).offset(offset));
			for (Entity entity : result) {
				JSONObject ent = new JSONObject();
				Map<String, Object> props = entity.getProperties();
				if(export) {
					ent.put("key", KeyFactory.keyToString(entity.getKey()));
					ent.put("entitytype", type);
				}
				for (String key : props.keySet()) {
					Object object = props.get(key);			
					String value = "";
					if(object !=null)value = object.toString();
					if(object instanceof Text) value = ((Text)object).getValue();
					if(object instanceof Date) value = DateManager.fmt1.print(new DateTime((Date)object));
					ent.put(key, value);
					if(export)ent.put(key + "_type", object.getClass().getCanonicalName());
				}
				results.add(ent);
			}
		} catch (JSONException e) {
			log.log(Level.SEVERE,"JSONException",e);
		}
		return results;
	}
	
	public String getUrlAsString(URL url, Charset charSet) {
		String result = "";
	      try {
	            BufferedReader reader = null;
	            if(charSet == null) {
	            	reader = new BufferedReader(new InputStreamReader(url.openStream()));
	            }else {
	            	reader = new BufferedReader(new InputStreamReader(url.openStream(),charSet));
	            }
	            String line;

	            while ((line = reader.readLine()) != null) {
	                result += line + "\n";
	            }
	            reader.close();

	        } catch (MalformedURLException e) {
	            log.log(Level.SEVERE, "Url is malformed",e);
	        } catch (IOException e) {
	        	log.log(Level.SEVERE, "IOException",e);
	        }
	        return result;
	}
}

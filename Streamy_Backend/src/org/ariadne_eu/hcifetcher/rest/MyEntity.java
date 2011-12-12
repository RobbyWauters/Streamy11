package org.ariadne_eu.hcifetcher.rest;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.ariadne_eu.hcifetcher.util.DateManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@Path("/entity")
public class MyEntity {

	private static final Logger log = Logger.getLogger(MyEntity.class.getName());

	@GET
	@Path ("/")
	@Produces ("application/json")
	public String getAll(@QueryParam("f") String formatStr,@QueryParam("ent") String entityStr){//,@QueryParam("p") String pageStr,@QueryParam("s") String sizeStr
		JSONObject result = new JSONObject();

		//		int size = 100;
		//		int page = 1;
		//		try {
		//			page = Integer.parseInt(pageStr);
		//		} catch (NumberFormatException e) {
		//			// TODO: handle exception
		//		}
		//		try {
		//			size = Integer.parseInt(sizeStr);
		//		} catch (NumberFormatException e) {
		//			// TODO: handle exception
		//		}

		Vector<JSONObject> results = new Vector<JSONObject>();
		Vector<String>kinds = new Vector<String>();
		if(entityStr != null && !entityStr.isEmpty()) {
			kinds.add(entityStr);
		}else {
			kinds.add("Blog");
			kinds.add("Comment");
			kinds.add("Tweet");
			kinds.add("File");
			kinds.add("Schedule");
			kinds.add("StreamyComment");
			if(formatStr != null && formatStr.equalsIgnoreCase("export")) {
				kinds.add("Entry");
				kinds.add("LastUpdated");
			}
		}
		for (String kind : kinds) {
			results.addAll(RestUtils.getInstance().getEntriesJSON(kind,null,null,formatStr));	
		}

		try {
			result.put("total_results",results.size());
			result.put("results", results);
			return result.toString(2);
		} catch (JSONException e) {
			log.log(Level.SEVERE,"JSONException",e);
		}
		return "";
	}	


	@POST
	@Path ("/import")
	@Produces ("application/json")
	public String importEntries(@FormParam("import") String importStr,@FormParam("secret") String secret){
		JSONObject result = new JSONObject();
		if(secret == null || !secret.equals("StReAmY")) {
			try {
				result.put("result", "Authentication failed ...");
			} catch (JSONException e) {
				log.log(Level.SEVERE,"JSONException",e);
			}
		} else {
			try {
				if(importStr != null) {
					JSONObject importJ = new JSONObject(importStr);

					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

					JSONArray results = importJ.getJSONArray("results");

					for(int i = 0; i < results.length(); i++) {
						JSONObject entityJson = results.getJSONObject(i);
						String entityKey = entityJson.getString("key");
						String entitytype = entityJson.getString("entitytype");
						Key stringToKey = KeyFactory.stringToKey(entityKey);

						String keyKind = stringToKey.getKind();
						String keyName = stringToKey.getName();
						long keyId = stringToKey.getId();

						if(stringToKey.getParent() != null) {
							String parentKind = stringToKey.getParent().getKind();
							String parentName = stringToKey.getParent().getName();
							long parentId = stringToKey.getParent().getId();
							Key parentKey = null;
							if(parentId == 0) {
								parentKey = KeyFactory.createKey(parentKind, parentName);	
							}else {
								parentKey = KeyFactory.createKey(parentKind, parentId);
							}
							if(keyId == 0) {
								stringToKey = KeyFactory.createKey(parentKey, keyKind, keyName);
							}else {
								stringToKey = KeyFactory.createKey(parentKey, keyKind, keyId);	
							}
						}else{
							if(keyId == 0) {
								stringToKey = KeyFactory.createKey(keyKind, keyName);
							}else {
								stringToKey = KeyFactory.createKey( keyKind, keyId);	
							}
						}
						Entity entity = new Entity(stringToKey);
						String[] keys = JSONObject.getNames(entityJson);
						for (String key : keys) {
							if(!key.endsWith("_type") && entityJson.has(key + "_type")) {
								String type = entityJson.getString(key + "_type");
								String valueStr = entityJson.getString(key);
								Object value = null;
								if(type != null) {
									if(type.equalsIgnoreCase("java.lang.String")) {
										value = new String(valueStr);
									}else if(type.equalsIgnoreCase("com.google.appengine.api.datastore.Text")){
										value = new Text(valueStr);
									}else if(type.equalsIgnoreCase("java.util.Date")){
										try {
											value = DateManager.fmt1.parseDateTime(valueStr).toDate();
										}catch (Exception e) {
											log.log(Level.SEVERE,"DateParsingError",e);
										}
									}

									if(value != null) entity.setProperty(key, value);
								}
							}
						}
						datastore.put(entity);
						//make a copy in Entry
						/*Entity entryEntity = new Entity("Entry");
						String[] entryKeys = JSONObject.getNames(entityJson);
						for (String key : entryKeys) {
							if(!key.endsWith("_type") && entityJson.has(key + "_type")) {
								String type = entityJson.getString(key + "_type");
								String valueStr = entityJson.getString(key);
								Object value = null;
								if(type != null) {
									if(type.equalsIgnoreCase("java.lang.String")) {
										value = new String(valueStr);
									}else if(type.equalsIgnoreCase("com.google.appengine.api.datastore.Text")){
										value = new Text(valueStr);
									}else if(type.equalsIgnoreCase("java.util.Date")){
										try {
											value = DateManager.fmt1.parseDateTime(valueStr).toDate();
										}catch (Exception e) {
											log.log(Level.SEVERE,"DateParsingError",e);
										}
									}

									if(value != null) entryEntity.setProperty(key, value);
								}
							}
						}
						datastore.put(entryEntity);*/
					}

				}
				else {
					result.put("result", "Like was empty, no User added...");
				}
			} catch (JSONException e) {
				try {
					result.put("result", "Json could not be parsed, error was : " + e.getMessage());
				} catch (JSONException e1) {
					log.log(Level.SEVERE,"JSONException",e);
				}
				log.log(Level.SEVERE,"JSONException",e);
			}
		}

		return result.toString();

	}

}

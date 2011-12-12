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

public class AddTweetServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(AddTweetServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.log(Level.INFO, "Cron job");
		String since_id = "119719744281640960";
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Tweet").addSort("guid", Query.SortDirection.DESCENDING);
		List<Entity> tweets = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));
		// TODO: long vs. string on guid!
		if (!tweets.isEmpty())
			since_id = (tweets.get(0).getProperty("guid")).toString();
		createTweetEntitiesfromHastTag("mume11", since_id);
		createTweetEntitiesfromHastTag("thesis11", since_id);
	}

	private void createTweetEntitiesfromHastTag(String hashtag, String since_id) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		log.log(Level.INFO, "createTweetEntitiesfromHastTag");
		TwitterSearch ts = new TwitterSearch();
		JSONArray results = ts.getTweetsFromHashtag(hashtag, since_id);
		for (int i = 0; i < results.length(); i++) {
			JSONObject resultObj = null;
			try {
				resultObj = results.getJSONObject(i);

//				Key entryKey = KeyFactory.createKey("Entry", resultObj.getLong("id"));

				DateTime parseDateTime = DateManager.fmt1.parseDateTime(resultObj.getString("created_at").split(", ")[1].trim());

				JSONArray htags = resultObj.getJSONObject("entities").getJSONArray("hashtags");
				String tags = "";
				for (int j = 0; j < htags.length(); j++) {
					JSONObject htag = htags.getJSONObject(j);
					if (!tags.isEmpty())
						tags += ",";
					tags += htag.getString("text");
				}

//				Entity entryEntity = new Entity("Entry", entryKey);
				Entity entryEntity = new Entity("Entry");
				entryEntity.setProperty("set", hashtag);
				entryEntity.setProperty("lastmodified", new Date());
				entryEntity.setProperty("guid", resultObj.getString("id"));
				entryEntity.setProperty("datestamp", parseDateTime.toDate());
				entryEntity.setProperty("author", resultObj.getString("from_user"));
				entryEntity.setProperty("text", new Text(resultObj.getString("text")));
				entryEntity.setProperty("tags", tags);
				entryEntity.setProperty("link", "http://twitter.com/#!/" + resultObj.getString("from_user") + "/status/" + resultObj.getLong("id"));
				entryEntity.setProperty("type", "Tweet");
				datastore.put(entryEntity);

//				Key tweetKey = KeyFactory.createKey("Tweet", resultObj.getLong("id"));

//				Entity tweetEntity = new Entity("Tweet", tweetKey);
				Entity tweetEntity = new Entity("Tweet");
				tweetEntity.setProperty("set", hashtag);
				tweetEntity.setProperty("lastmodified", new Date());
				tweetEntity.setProperty("guid", resultObj.getString("id"));
				tweetEntity.setProperty("datestamp", parseDateTime.toDate());
				tweetEntity.setProperty("author", resultObj.getString("from_user"));
				tweetEntity.setProperty("text", new Text(resultObj.getString("text")));
				// TODO: extract hashtags from text
				tweetEntity.setProperty("tags", tags);
				tweetEntity.setProperty("type", "Tweet");
				tweetEntity.setProperty("link", "http://twitter.com/#!/" + resultObj.getString("from_user") + "/status/" + resultObj.getLong("id"));
				tweetEntity.setProperty("isolanguagecode", resultObj.getString("iso_language_code"));
				if (resultObj.has("to_user"))
					tweetEntity.setProperty("to", resultObj.getString("to_user"));
				datastore.put(tweetEntity);
			} catch (JSONException e) {
				log.log(Level.SEVERE, "Json parse exception. Json=" + resultObj, e);
			}
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}

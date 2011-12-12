package org.ariadne_eu.hcifetcher.rss;

import it.sauronsoftware.feed4j.FeedIOException;
import it.sauronsoftware.feed4j.FeedParser;
import it.sauronsoftware.feed4j.FeedXMLParseException;
import it.sauronsoftware.feed4j.UnsupportedFeedException;
import it.sauronsoftware.feed4j.bean.Feed;
import it.sauronsoftware.feed4j.bean.FeedItem;
import it.sauronsoftware.feed4j.bean.RawElement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ariadne_eu.hcifetcher.util.DateManager;
import org.joda.time.DateTime;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;

public class RssFeeds {

	private static final Logger log = Logger.getLogger(RssFeeds.class.getName());

	public void updateRssFeeds(){
		try {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			Query q = new Query("RssFeed");
			List<Entity> feeds = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(1000));
			
			HashSet<String> types = new HashSet<String>();
			
			Iterator<Entity> iterator = feeds.iterator();
			while(iterator.hasNext()) {
				Entity feed = iterator.next();
				Feed rss = FeedParser.parse(new URL((String)feed.getProperty("feed")));
				String type = (String)feed.getProperty("type");
				String set = (String)feed.getProperty("set");
				for (int i = 0; i < rss.getItemCount();i++) {
					FeedItem item = rss.getItem(i);
					String pubDate = item.getElementValue("", "pubDate");
					pubDate = pubDate.split(", ")[1];
					DateTime parseDateTime = DateManager.fmt1.parseDateTime(pubDate);
					if(DateManager.getInstance().isMoreRecent(parseDateTime, type)) {
						addItem( item, parseDateTime,type,set);
						types.add(type);
					}
				}
			}
			
			for (String type : types) {
				DateManager.getInstance().updateTo(new DateTime(), type);
			}
			
		} catch (MalformedURLException e) {
			log.log(Level.SEVERE,"MalformedURLException",e);
		} catch (FeedIOException e) {
			log.log(Level.SEVERE,"FeedIOException",e);
		} catch (FeedXMLParseException e) {
			log.log(Level.SEVERE,"FeedXMLParseException",e);
		} catch (UnsupportedFeedException e) {
			log.log(Level.SEVERE,"UnsupportedFeedException",e);
		}
	}

	private void addItem( FeedItem item, DateTime parseDateTime, String type, String set) {
		boolean blog = true;
		if(!type.equalsIgnoreCase("Blog")) blog = false;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		String guid = item.getGUID();
		Date datestamp = parseDateTime.toDate();
		String author = item.getElementValue("http://purl.org/dc/elements/1.1/", "creator");
		String text = item.getDescriptionAsText();
		String tags = "";

		if(blog) {
			RawElement[] elements = item.getElements("", "category");
			for (RawElement rawElement : elements) {
				tags += rawElement.getValue() + ";";
			}
		}

		String link = item.getLink().toString();
		Date lastModified = new DateTime().toDate();

//		Key entryKey = KeyFactory.createKey("Entry", guid);
//		Entity entryEntity = new Entity("Entry", entryKey);
		Entity entryEntity = new Entity("Entry");

		entryEntity.setProperty("set", set);
		entryEntity.setProperty("guid", guid);
		entryEntity.setProperty("datestamp", datestamp);
		entryEntity.setProperty("author", author);
		entryEntity.setProperty("text", new Text(text));
		entryEntity.setProperty("tags", tags);
		entryEntity.setProperty("link", link);
		entryEntity.setProperty("type", type);
		entryEntity.setProperty("lastmodified",lastModified);
		datastore.put(entryEntity);

		String typeEntity = "Blog";
		if(!blog)typeEntity = "Comment";
//		Key blogKey = KeyFactory.createKey(typeEntity, guid);
//		Entity blogEntity = new Entity(typeEntity, blogKey);
		Entity blogEntity = new Entity(typeEntity);

		log.info("Stored " + guid);

		blogEntity.setProperty("set", set);
		blogEntity.setProperty("guid", guid);
		blogEntity.setProperty("datestamp", datestamp);
		blogEntity.setProperty("author", author);
		blogEntity.setProperty("text", new Text(text));
		blogEntity.setProperty("tags", tags);
		blogEntity.setProperty("link", link);
		blogEntity.setProperty("type", type);
		blogEntity.setProperty("lastmodified",lastModified);

		if(blog) {
			blogEntity.setProperty("title", item.getTitle());
		}else {
			//			blogEntity.setProperty("", value)
		}
		datastore.put(blogEntity);
	}
}

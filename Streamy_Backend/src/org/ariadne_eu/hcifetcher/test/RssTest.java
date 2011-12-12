package org.ariadne_eu.hcifetcher.test;

import it.sauronsoftware.feed4j.FeedIOException;
import it.sauronsoftware.feed4j.FeedParser;
import it.sauronsoftware.feed4j.FeedXMLParseException;
import it.sauronsoftware.feed4j.UnsupportedFeedException;
import it.sauronsoftware.feed4j.bean.Feed;
import it.sauronsoftware.feed4j.bean.FeedItem;
import it.sauronsoftware.feed4j.bean.RawElement;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RssTest {
	
	public static void main(String[] args) {
		testFeed4j();
	}
	
	public static void testFeed4j() {
		try {
			
			Feed rss = FeedParser.parse(new URL("http://mobiledo.wordpress.com/feed/"));
			
			System.out.println();			System.out.println();			System.out.println();
			for (int i = 0; i < rss.getItemCount();i++) {
				
				FeedItem item = rss.getItem(i);
				
				System.out.println(item.getDescriptionAsText());
				System.out.println(item.getGUID());
				System.out.println(item.getName());
				System.out.println(item.getTitle());
				System.out.println(item.getLink());
				System.out.println(item.getModDate());
				System.out.println(item.getPubDate());
				System.out.println(item.getElementValue("http://purl.org/dc/elements/1.1/", "creator"));
				RawElement[] elements = item.getElements("", "category");
				for (RawElement rawElement : elements) {
					System.out.println(rawElement.getValue());
				}
//				System.out.println(item.getComments());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedXMLParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedFeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
//	public static void testRssUtils() {
//		try {
//			RssParser parser = RssParserFactory.createDefault();
//			Rss rss = parser.parse(new URL("http://appsint.wordpress.com/feed/"));
//			Collection<Item> items = rss.getChannel().getItems();
//			
//			System.out.println();			System.out.println();			System.out.println();
//			for (Item item : items) {
//				
////				System.out.println(item.getText());
//				System.out.println(item.getAuthor());
//				System.out.println(item.getCategories());
////				System.out.println(item.getChildren());
//				System.out.println(item.getComments());
//				System.out.println(item.getDescription());
//				System.out.println(item.getEnclosure());
//				System.out.println(item.getGuid());
//				System.out.println(item.getLink());
////				System.out.println(item.getParent());
//				System.out.println(item.getPubDate());
//				System.out.println(item.getSource());
//				System.out.println(item.getTitle());
//			}
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (RssParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}

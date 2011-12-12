package org.ariadne_eu.hcifetcher.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ariadne_eu.hcifetcher.rss.RssFeeds;

public class AddRssFeedServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		RssFeeds feeds = new RssFeeds();
		feeds.updateRssFeeds();
	}

	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException {
		//doPost(req,resp);
		RssFeeds feeds = new RssFeeds();
		feeds.updateRssFeeds();
	}

}
